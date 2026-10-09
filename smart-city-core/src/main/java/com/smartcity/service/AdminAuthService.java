package com.smartcity.service;

import com.smartcity.exception.AuthenticationException;
import com.smartcity.exception.ValidationException;
import com.smartcity.model.Administrator;
import com.smartcity.security.PasswordHasher;

/**
 * Admin authentication.
 *
 * <p>For backward compatibility with the original Java LoginFrame, admin
 * credentials are still hardcoded ({@code admin / admin123}). The hash
 * below is the BCrypt hash of {@code admin123} (cost 12); we still go
 * through {@link PasswordHasher#verify} so the path is identical to
 * the citizen flow and migrating admin accounts to the database later
 * only requires swapping this class's internals.
 *
 * <p>MIGRATION NOTE: when admin accounts move to the {@code admins} table,
 * replace {@link #lookupByUsername(String)} with a JdbcAdminRepository call
 * and delete the constant below.
 */
public class AdminAuthService {

    /** BCrypt hash of "admin123" with cost 12. */
    private static final String ADMIN_PASSWORD_HASH =
            "$2a$12$GuoZqFBO.9S.YtcNXlH74u0FkOuypZBlC0DYOml3WyIEX.cz5Bpqq";

    /** Hardcoded admin username. */
    public static final String ADMIN_USERNAME = "admin";

    /**
     * @return an {@link Administrator} on successful auth
     * @throws AuthenticationException on wrong username or wrong password
     */
    public Administrator login(String username, String password) {
        requireNonBlank(username, "Username");
        requireNonBlank(password, "Password");

        if (!ADMIN_USERNAME.equals(username) || !PasswordHasher.verify(password, ADMIN_PASSWORD_HASH)) {
            throw new AuthenticationException();
        }
        return new Administrator(0, ADMIN_USERNAME, "Administrator",
                "admin@smartcity", ADMIN_PASSWORD_HASH);
    }

    private static void requireNonBlank(String v, String name) {
        if (v == null || v.isBlank()) {
            throw new ValidationException(name + " is required.");
        }
    }
}
