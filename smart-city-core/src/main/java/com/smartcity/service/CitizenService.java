package com.smartcity.service;

import com.smartcity.exception.DatabaseException;
import com.smartcity.exception.NotFoundException;
import com.smartcity.exception.ValidationException;
import com.smartcity.interfaces.CitizenRepository;
import com.smartcity.model.Citizen;
import com.smartcity.security.PasswordHasher;

import java.util.List;
import java.util.Objects;

/**
 * Service-layer CRUD for {@link Citizen}. Wraps the
 * {@link CitizenRepository} abstraction so the GUI and Web modules depend
 * on the service, not the JDBC implementation.
 *
 * <p>Responsibilities:
 * <ul>
 *   <li>Run server-side validation via {@link Validator};</li>
 *   <li>Hash any plaintext password before handing it to the repository
 *       (no plaintext password ever reaches the DAO);</li>
 *   <li>Throw typed exceptions on validation / not-found / DB failures so
 *       callers can render user-friendly errors instead of NPEs.</li>
 * </ul>
 */
public class CitizenService {

    private final CitizenRepository citizens;

    public CitizenService(CitizenRepository citizens) {
        this.citizens = Objects.requireNonNull(citizens, "citizens repository");
    }

    /**
     * Registers a new citizen.
     *
     * @param plaintextPassword the user's plaintext password (will be hashed
     *                           before persistence)
     * @return the created {@link Citizen} with its generated id populated
     * @throws ValidationException if any field is invalid or the password is blank
     * @throws DatabaseException    on duplicate email / underlying JDBC failure
     */
    public Citizen addCitizen(String name, String email, String phone, String plaintextPassword, String address) {
        Validator.validateCitizenFields(name, email, phone, address);
        if (plaintextPassword == null || plaintextPassword.length() < 6) {
            throw new ValidationException("Password must be at least 6 characters.");
        }

        // Reject duplicate email at the service layer too — DB unique constraint is
        // the last line of defence, but a friendly error is nicer than a raw SQLException.
        citizens.findByEmail(email).ifPresent(c -> {
            throw new ValidationException("Email is already registered.");
        });

        Citizen c = new Citizen();
        c.setName(name.trim());
        c.setEmail(email.trim().toLowerCase());
        c.setPhone(phone.trim());
        c.setPasswordHash(PasswordHasher.hash(plaintextPassword));
        c.setAddress(address.trim());
        return citizens.save(c);
    }

    /**
     * @return every citizen in the database, ordered by id ascending.
     */
    public List<Citizen> getAllCitizens() {
        return citizens.findAll();
    }

    /**
     * @return the citizen with the given id
     * @throws NotFoundException if no row exists
     */
    public Citizen getCitizenById(int id) {
        return citizens.findById(id)
                .orElseThrow(() -> new NotFoundException("Citizen", id));
    }

    /**
     * Updates the editable fields of an existing citizen. Does NOT update
     * the password hash — see {@link #changePassword(int, String, String)}.
     */
    public void updateCitizen(int id, String name, String email, String phone, String address) {
        Validator.requirePositiveId(id, "Citizen ID");
        Validator.validateCitizenFields(name, email, phone, address);

        Citizen c = getCitizenById(id);
        c.setName(name.trim());
        c.setEmail(email.trim().toLowerCase());
        c.setPhone(phone.trim());
        c.setAddress(address.trim());
        citizens.update(c);
    }

    /**
     * Changes a citizen's password after verifying the old password.
     *
     * @throws ValidationException if the new password is too short or the
     *                              old password is wrong
     */
    public void changePassword(int id, String oldPassword, String newPassword) {
        Validator.requirePositiveId(id, "Citizen ID");
        if (newPassword == null || newPassword.length() < 6) {
            throw new ValidationException("New password must be at least 6 characters.");
        }
        Citizen c = getCitizenById(id);
        if (!PasswordHasher.verify(oldPassword, c.getPasswordHash())) {
            throw new ValidationException("Old password is incorrect.");
        }
        c.setPasswordHash(PasswordHasher.hash(newPassword));
        citizens.update(c);
    }

    /**
     * Deletes a citizen (cascades to complaints/feedback).
     * @throws NotFoundException if no row exists
     */
    public void deleteCitizen(int id) {
        Validator.requirePositiveId(id, "Citizen ID");
        // findById throws NotFoundException if missing — better UX than a raw
        // DAO delete returning 0 rows.
        getCitizenById(id);
        citizens.deleteById(id);
    }
}
