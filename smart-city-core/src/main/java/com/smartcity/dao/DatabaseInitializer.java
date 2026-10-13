package com.smartcity.dao;

import com.smartcity.config.DatabaseConfig;
import com.smartcity.exception.DatabaseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Loads the canonical SQL schema (and optionally seed data) into the
 * target database. Used by:
 *
 * <ul>
 *   <li>The H2-backed test base, to spin up a clean in-memory schema
 *       before each test class;</li>
 *   <li>Production operators, manually, on a fresh MySQL database.</li>
 * </ul>
 *
 * <p>The script is split on {@code ;} into individual statements, which is
 * sufficient for the simple DDL/DML used here. For a more complex schema
 * one would use a real migration tool (Flyway / Liquibase).
 */
public final class DatabaseInitializer {

    private final DatabaseConfig db;

    public DatabaseInitializer(DatabaseConfig db) {
        this.db = db;
    }

    /**
     * Runs the given classpath script. Splits on {@code ;} into statements.
     * Comments starting with {@code --} are stripped before splitting.
     */
    public void runScript(String classpathLocation) {
        String script = readResource(classpathLocation);
        String cleaned = stripComments(script);
        for (String stmt : cleaned.split(";")) {
            String trimmed = stmt.trim();
            if (trimmed.isEmpty()) continue;
            try (Connection con = db.openConnection();
                 Statement st = con.createStatement()) {
                st.execute(trimmed);
            } catch (SQLException e) {
                throw new DatabaseException("Failed to execute statement from " + classpathLocation, e);
            }
        }
    }

    private static String stripComments(String script) {
        StringBuilder out = new StringBuilder(script.length());
        for (String line : script.split("\\R")) {
            int dash = line.indexOf("--");
            if (dash >= 0) {
                line = line.substring(0, dash);
            }
            out.append(line).append('\n');
        }
        return out.toString();
    }

    private static String readResource(String path) {
        try (InputStream in = DatabaseInitializer.class.getClassLoader().getResourceAsStream(path)) {
            if (in == null) {
                throw new DatabaseException("SQL resource not found on classpath: " + path);
            }
            StringBuilder sb = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append('\n');
                }
            }
            return sb.toString();
        } catch (IOException e) {
            throw new DatabaseException("Could not read SQL resource " + path, e);
        }
    }
}
