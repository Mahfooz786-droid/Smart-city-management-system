package com.smartcity.dao;

import com.smartcity.config.DatabaseConfig;
import com.smartcity.exception.DatabaseException;
import com.smartcity.interfaces.ComplaintRepository;
import com.smartcity.model.Complaint;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of {@link ComplaintRepository}.
 *
 * <p>Status column stores the human-readable string ("Pending" / "In Progress"
 * / "Resolved") for backward compatibility with the original schema. The
 * service layer converts to / from the type-safe {@link Complaint.Status}
 * enum.
 */
public class JdbcComplaintRepository implements ComplaintRepository {

    private final DatabaseConfig db;

    public JdbcComplaintRepository(DatabaseConfig db) {
        this.db = db;
    }

    @Override
    public Complaint save(Complaint complaint) {
        final String sql = "INSERT INTO complaints (citizen_id, subject, description, status) " +
                           "VALUES (?, ?, ?, ?)";
        try (Connection con = db.openConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, complaint.getCitizenId());
            ps.setString(2, complaint.getSubject());
            ps.setString(3, complaint.getDescription());
            ps.setString(4, Complaint.Status.PENDING.dbValue());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    complaint.setId(keys.getInt(1));
                }
            }
            complaint.setStatus(Complaint.Status.PENDING);
            complaint.setCreatedAt(LocalDateTime.now());
            return complaint;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to insert complaint.", e);
        }
    }

    @Override
    public List<Complaint> findByCitizenId(int citizenId) {
        final String sql = "SELECT complaint_id, citizen_id, subject, description, status, created_at " +
                           "FROM complaints WHERE citizen_id = ? ORDER BY complaint_id DESC";
        List<Complaint> out = new ArrayList<>();
        try (Connection con = db.openConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, citizenId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(mapRow(rs, null));
                }
            }
            return out;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to load complaints for citizenId=" + citizenId + ".", e);
        }
    }

    @Override
    public List<Complaint> findAll() {
        final String sql = "SELECT c.complaint_id, c.citizen_id, ci.name AS citizen_name, " +
                           "       c.subject, c.description, c.status, c.created_at " +
                           "FROM complaints c " +
                           "JOIN citizens ci ON c.citizen_id = ci.citizen_id " +
                           "ORDER BY c.complaint_id DESC";
        List<Complaint> out = new ArrayList<>();
        try (Connection con = db.openConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(mapRow(rs, "citizen_name"));
            }
            return out;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to load all complaints.", e);
        }
    }

    @Override
    public void updateStatus(int complaintId, Complaint.Status newStatus) {
        final String sql = "UPDATE complaints SET status = ? WHERE complaint_id = ?";
        try (Connection con = db.openConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, newStatus.dbValue());
            ps.setInt(2, complaintId);
            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new DatabaseException("Complaint id=" + complaintId + " was not updated (not found).");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to update status of complaint id=" + complaintId + ".", e);
        }
    }

    private Complaint mapRow(ResultSet rs, String citizenNameCol) throws SQLException {
        Timestamp ts = rs.getTimestamp("created_at");
        return new Complaint(
                rs.getInt("complaint_id"),
                rs.getInt("citizen_id"),
                citizenNameCol != null ? rs.getString(citizenNameCol) : null,
                rs.getString("subject"),
                rs.getString("description"),
                Complaint.Status.fromDb(rs.getString("status")),
                ts != null ? ts.toLocalDateTime() : null
        );
    }
}
