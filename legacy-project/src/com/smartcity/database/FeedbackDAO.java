package com.smartcity.database;

import com.smartcity.dao.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class FeedbackDAO {

    // =========================
    // ADD FEEDBACK
    // =========================

    public static void addFeedback(
            int citizenId,
            int rating,
            String comment) {

        String sql =
                "INSERT INTO feedback " +
                        "(citizen_id, rating, comment) " +
                        "VALUES (?, ?, ?)";

        try {

            Connection con =
                    DatabaseConnection.getConnection();

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setInt(1, citizenId);
            ps.setInt(2, rating);
            ps.setString(3, comment);

            ps.executeUpdate();

            ps.close();

            System.out.println(
                    "Feedback Added Successfully!"
            );

        } catch (Exception e) {

            e.printStackTrace();
        }
    }


    // =========================
    // GET ALL FEEDBACK
    // =========================

    public static List<String> getAllFeedback() {

        List<String> feedbackList =
                new ArrayList<>();

        String sql =
                "SELECT f.feedback_id, " +
                        "f.citizen_id, " +
                        "c.name, " +
                        "f.rating, " +
                        "f.comment, " +
                        "f.feedback_date " +
                        "FROM feedback f " +
                        "JOIN citizens c " +
                        "ON f.citizen_id = c.citizen_id " +
                        "ORDER BY f.feedback_id DESC";

        try {

            Connection con =
                    DatabaseConnection.getConnection();

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ResultSet rs =
                    ps.executeQuery();

            while (rs.next()) {

                String feedback =
                        "Feedback ID: "
                                + rs.getInt("feedback_id")

                                + "\nCitizen ID: "
                                + rs.getInt("citizen_id")

                                + "\nCitizen Name: "
                                + rs.getString("name")

                                + "\nRating: "
                                + rs.getInt("rating")
                                + "/5"

                                + "\nComment: "
                                + rs.getString("comment")

                                + "\nDate: "
                                + rs.getString("feedback_date");

                feedbackList.add(feedback);
            }

            rs.close();
            ps.close();

        } catch (Exception e) {

            e.printStackTrace();
        }

        return feedbackList;
    }
}