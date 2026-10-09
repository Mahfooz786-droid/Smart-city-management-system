package com.smartcity.service;

import com.smartcity.exception.AuthenticationException;
import com.smartcity.exception.DatabaseException;
import com.smartcity.exception.ValidationException;
import com.smartcity.interfaces.CitizenRepository;
import com.smartcity.model.Citizen;
import com.smartcity.security.PasswordHasher;

import java.util.Optional;

/**
 * Service-layer authentication for citizens.
 *
 * <p>The DAO {@code CitizenLoginService.loginCitizen(email, password)} in the
 * original project did the BCrypt comparison INSIDE the SQL query by
 * including the password in the WHERE clause. That is broken for hashed
 * passwords — we cannot select by hash in advance because each hash has a
 * different random salt. So we now:
 *
 * <ol>
 *   <li>look up the citizen by email only;</li>
 *   <li>verify the supplied plaintext password against the stored
 *       {@code password_hash} in constant time via {@link PasswordHasher};</li>
 *   <li>return the citizen on success or throw
 *       {@link AuthenticationException} with a generic message on failure.</li>
 * </ol>
 *
 * <p>Generic error message prevents account enumeration.
 */
public class CitizenAuthService {

    private final CitizenRepository citizens;

    public CitizenAuthService(CitizenRepository citizens) {
        this.citizens = citizens;
    }

    /**
     * @param email    citizen email
     * @param password plaintext password supplied by the user at login
     * @return the authenticated {@link Citizen}
     * @throws AuthenticationException on missing account or wrong password
     * @throws DatabaseException       on underlying JDBC failure
     */
    public Citizen login(String email, String password) {
        requireNonBlank(email, "Email");
        requireNonBlank(password, "Password");

        Optional<Citizen> found = citizens.findByEmail(email);
        if (found.isEmpty()) {
            throw new AuthenticationException(); // generic
        }
        Citizen citizen = found.get();
        if (!PasswordHasher.verify(password, citizen.getPasswordHash())) {
            throw new AuthenticationException(); // generic
        }
        return citizen;
    }

    /**
     * Verifies the given plaintext candidate matches the stored hash for the
     * given citizen id. Used by the web layer's session-update flows.
     */
    public boolean verifyPassword(int citizenId, String plaintextCandidate) {
        return citizens.findById(citizenId)
                .map(c -> PasswordHasher.verify(plaintextCandidate, c.getPasswordHash()))
                .orElse(false);
    }

    private static void requireNonBlank(String v, String name) {
        if (v == null || v.isBlank()) {
            throw new ValidationException(name + " is required.");
        }
    }
}
