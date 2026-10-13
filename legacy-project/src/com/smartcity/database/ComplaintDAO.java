package com.smartcity.database;

import com.smartcity.dao.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ComplaintDAO {

    // Citizen: Register Complaint
    public static void addComplaint(
            int citizenId,
            String subject,
            String description) {

        String sql =
                "INSERT INTO complaints " +
                        "(citizen_id, subject, description, status) " +
                        "VALUES (?, ?, ?, ?)";

        try {
            Connection con =
                    DatabaseConnection.getConnection();

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setInt(1, citizenId);
            ps.setString(2, subject);
            ps.setString(3, description);
            ps.setString(4, "Pending");

            ps.executeUpdate();

            ps.close();

            System.out.println(
                    "Complaint Added Successfully!"
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Citizen: View Own Complaints
    public static List<String> getComplaintsByCitizenId(
            int citizenId) {

        List<String> complaints =
                new ArrayList<>();

        String sql =
                "SELECT complaint_id, subject, " +
                        "description, status, created_at " +
                        "FROM complaints " +
                        "WHERE citizen_id = ? " +
                        "ORDER BY complaint_id DESC";

        try {
            Connection con =
                    DatabaseConnection.getConnection();

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setInt(1, citizenId);

            ResultSet rs =
                    ps.executeQuery();

            while (rs.next()) {

                String complaint =
                        "Complaint ID: "
                                + rs.getInt("complaint_id")
                                + "\nSubject: "
                                + rs.getString("subject")
                                + "\nDescription: "
                                + rs.getString("description")
                                + "\nStatus: "
                                + rs.getString("status")
                                + "\nCreated At: "
                                + rs.getString("created_at");

                complaints.add(complaint);
            }

            rs.close();
            ps.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return complaints;
    }

    // Admin: View All Complaints
    public static List<String> getAllComplaints() {

        List<String> complaints =
                new ArrayList<>();

        String sql =
                "SELECT c.complaint_id, " +
                        "c.citizen_id, " +
                        "ci.name, " +
                        "c.subject, " +
                        "c.description, " +
                        "c.status, " +
                        "c.created_at " +
                        "FROM complaints c " +
                        "JOIN citizens ci " +
                        "ON c.citizen_id = ci.citizen_id " +
                        "ORDER BY c.complaint_id DESC";

        try {

            Connection con =
                    DatabaseConnection.getConnection();

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ResultSet rs =
                    ps.executeQuery();

            while (rs.next()) {

                String complaint =
                        "Complaint ID: "
                                + rs.getInt("complaint_id")
                                + "\nCitizen ID: "
                                + rs.getInt("citizen_id")
                                + "\nCitizen Name: "
                                + rs.getString("name")
                                + "\nSubject: "
                                + rs.getString("subject")
                                + "\nDescription: "
                                + rs.getString("description")
                                + "\nStatus: "
                                + rs.getString("status")
                                + "\nCreated At: "
                                + rs.getString("created_at");

                complaints.add(complaint);
            }

            rs.close();
            ps.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return complaints;
    }

    // Admin: Update Complaint Status
    public static void updateComplaintStatus(
            int complaintId,
            String status) {

        String sql =
                "UPDATE complaints " +
                        "SET status = ? " +
                        "WHERE complaint_id = ?";

        try {

            Connection con =
                    DatabaseConnection.getConnection();

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setString(1, status);
            ps.setInt(2, complaintId);

            ps.executeUpdate();

            ps.close();

            System.out.println(
                    "Complaint Status Updated Successfully!"
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}