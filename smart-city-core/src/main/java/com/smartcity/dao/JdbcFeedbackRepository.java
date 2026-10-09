package com.smartcity.dao;

import com.smartcity.config.DatabaseConfig;
import com.smartcity.exception.DatabaseException;
import com.smartcity.interfaces.FeedbackRepository;
import com.smartcity.model.Feedback;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** JDBC implementation of {@link FeedbackRepository}. */
public class JdbcFeedbackRepository implements FeedbackRepository {

    private final DatabaseConfig db;

    public JdbcFeedbackRepository(DatabaseConfig db) {
        this.db = db;
    }

    @Override
    public Feedback save(Feedback feedback) {
        final String sql = "INSERT INTO feedback (citizen_id, rating, comment) VALUES (?, ?, ?)";
        try (Connection con = db.openConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, feedback.getCitizenId());
            ps.setInt(2, feedback.getRating());
            ps.setString(3, feedback.getComment());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    feedback.setId(keys.getInt(1));
                }
            }
            feedback.setCreatedAt(LocalDateTime.now());
            return feedback;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to insert feedback.", e);
        }
    }

    @Override
    public List<Feedback> findAll() {
        final String sql = "SELECT f.feedback_id, f.citizen_id, c.name AS citizen_name, " +
                           "       f.rating, f.comment, f.feedback_date " +
                           "FROM feedback f " +
                           "JOIN citizens c ON f.citizen_id = c.citizen_id " +
                           "ORDER BY f.feedback_id DESC";
        List<Feedback> out = new ArrayList<>();
        try (Connection con = db.openConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Timestamp ts = rs.getTimestamp("feedback_date");
                out.add(new Feedback(
                        rs.getInt("feedback_id"),
                        rs.getInt("citizen_id"),
                        rs.getString("citizen_name"),
                        rs.getInt("rating"),
                        rs.getString("comment"),
                        ts != null ? ts.toLocalDateTime() : null
                ));
            }
            return out;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to load all feedback.", e);
        }
    }
}
