package com.smartcity.config;

import com.smartcity.exception.DatabaseException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

/**
 * Loads database configuration from environment variables (with sensible
 * development defaults) and produces JDBC connections.
 *
 * <p><b>No credentials are hardcoded in this class.</b> The following
 * environment variables are consulted at construction time:
 *
 * <ul>
 *   <li>{@code DB_URL}        – JDBC URL, e.g. {@code jdbc:mysql://localhost:3306/smart_city}</li>
 *   <li>{@code DB_USER}       – DB username</li>
 *   <li>{@code DB_PASSWORD}   – DB password</li>
 * </ul>
 *
 * For local development, defaults of {@code jdbc:mysql://localhost:3306/smart_city},
 * {@code root}, and an empty password are used ONLY if no env vars are set.
 * Production deployments MUST set all three env vars.
 *
 * <p>An alternative constructor {@link #DatabaseConfig(String, String, String)}
 * is provided for unit tests and for the in-memory H2 test mode.
 */
public class DatabaseConfig {

    private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/smart_city?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String DEFAULT_USER = "root";
    private static final String DEFAULT_PASSWORD = "";

    private final String url;
    private final String user;
    private final String password;

    /**
     * Production constructor. Reads from environment variables with safe
     * development defaults. The defaults are intentionally restrictive
     * (empty password) so missing env vars produce an obvious auth failure
     * rather than a silent success.
     */
    public DatabaseConfig() {
        this(
            System.getenv().getOrDefault("DB_URL", DEFAULT_URL),
            System.getenv().getOrDefault("DB_USER", DEFAULT_USER),
            System.getenv().getOrDefault("DB_PASSWORD", DEFAULT_PASSWORD)
        );
    }

    /**
     * Test / explicit constructor. Used by the DAO test base to inject
     * an H2 in-memory URL, or by callers that already have a known
     * connection profile.
     */
    public DatabaseConfig(String url, String user, String password) {
        this.url = Objects.requireNonNull(url, "DB url must not be null");
        this.user = Objects.requireNonNull(user, "DB user must not be null");
        // password may be empty (H2 default), but must not be null
        this.password = password == null ? "" : password;
    }

    public String getUrl() { return url; }
    public String getUser() { return user; }
    public String getPassword() { return password; }

    /**
     * Opens a fresh JDBC connection. The caller is responsible for closing
     * it (almost always via try-with-resources).
     *
     * @throws DatabaseException if the underlying driver cannot establish
     *                           a connection. The original
     *                           {@link SQLException} is preserved as the
     *                           cause so callers can log the details.
     */
    public Connection openConnection() throws DatabaseException {
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            // Never log the password or URL containing credentials.
            throw new DatabaseException("Unable to establish database connection.", e);
        }
    }
}
