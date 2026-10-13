package com.smartcity.dao;

import com.smartcity.config.DatabaseConfig;
import com.smartcity.exception.DatabaseException;
import com.smartcity.model.Citizen;
import com.smartcity.interfaces.CitizenRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JDBC implementation of {@link CitizenRepository}.
 *
 * <p>Every method:
 * <ul>
 *   <li>opens its own short-lived {@link Connection} via {@link DatabaseConfig};</li>
 *   <li>uses {@link PreparedStatement} (parameterised) — no string-concatenated SQL;</li>
 *   <li>uses {@code try-with-resources} for {@code Connection},
 *       {@code PreparedStatement} and {@code ResultSet} — no leaked
 *       resources even on exception;</li>
 *   <li>throws {@link DatabaseException} on SQL failure — never returns a
 *       misleading "success" value;</li>
 *   <li>stores {@code password_hash} (a BCrypt hash), never the plaintext
 *       password.</li>
 * </ul>
 */
public class JdbcCitizenRepository implements CitizenRepository {

    private final DatabaseConfig db;

    public JdbcCitizenRepository(DatabaseConfig db) {
        this.db = db;
    }

    @Override
    public Citizen save(Citizen citizen) {
        final String sql = "INSERT INTO citizens (name, email, phone, password_hash, address) " +
                           "VALUES (?, ?, ?, ?, ?)";
        try (Connection con = db.openConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, citizen.getName());
            ps.setString(2, citizen.getEmail());
            ps.setString(3, citizen.getPhone());
            ps.setString(4, citizen.getPasswordHash());
            ps.setString(5, citizen.getAddress());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    citizen.setId(keys.getInt(1));
                }
            }
            return citizen;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to insert citizen '" + citizen.getEmail() + "'.", e);
        }
    }

    @Override
    public Optional<Citizen> findById(int id) {
        final String sql = "SELECT citizen_id, name, email, phone, password_hash, address " +
                           "FROM citizens WHERE citizen_id = ?";
        try (Connection con = db.openConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapRow(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to load citizen id=" + id + ".", e);
        }
    }

    @Override
    public Optional<Citizen> findByEmail(String email) {
        final String sql = "SELECT citizen_id, name, email, phone, password_hash, address " +
                           "FROM citizens WHERE email = ?";
        try (Connection con = db.openConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapRow(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to load citizen by email.", e);
        }
    }

    @Override
    public List<Citizen> findAll() {
        final String sql = "SELECT citizen_id, name, email, phone, password_hash, address " +
                           "FROM citizens ORDER BY citizen_id ASC";
        List<Citizen> out = new ArrayList<>();
        try (Connection con = db.openConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(mapRow(rs));
            }
            return out;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to load all citizens.", e);
        }
    }

    @Override
    public void update(Citizen citizen) {
        final String sql = "UPDATE citizens SET name=?, email=?, phone=?, address=? WHERE citizen_id=?";
        try (Connection con = db.openConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, citizen.getName());
            ps.setString(2, citizen.getEmail());
            ps.setString(3, citizen.getPhone());
            ps.setString(4, citizen.getAddress());
            ps.setInt(5, citizen.getId());
            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new DatabaseException("Citizen id=" + citizen.getId() + " was not updated.");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to update citizen id=" + citizen.getId() + ".", e);
        }
    }

    @Override
    public void deleteById(int id) {
        // Single-statement delete; the FK on complaints/feedback is ON DELETE CASCADE,
        // so this also removes the citizen's complaints and feedback atomically.
        final String sql = "DELETE FROM citizens WHERE citizen_id = ?";
        try (Connection con = db.openConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new DatabaseException("Citizen id=" + id + " was not deleted (not found).");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to delete citizen id=" + id + ".", e);
        }
    }

    /** Maps one JDBC row into a typed {@link Citizen}. */
    private Citizen mapRow(ResultSet rs) throws SQLException {
        return new Citizen(
                rs.getInt("citizen_id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("phone"),
                rs.getString("password_hash"),
                rs.getString("address")
        );
    }
}
