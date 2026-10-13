package com.smartcity.database;

import com.smartcity.dao.DatabaseConnection;
import com.smartcity.model.Citizen;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CitizenDAO {

    // ==========================================
    // ADD CITIZEN
    // ==========================================

    public static void addCitizen(
            String name,
            String email,
            String phone,
            String password,
            String address) {

        String sql =
                "INSERT INTO citizens " +
                        "(name, email, phone, password, address) " +
                        "VALUES (?, ?, ?, ?, ?)";

        try {

            Connection con =
                    DatabaseConnection.getConnection();

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setString(4, password);
            ps.setString(5, address);

            ps.executeUpdate();

            ps.close();

            System.out.println(
                    "Citizen Added Successfully!"
            );

        } catch (Exception e) {

            System.out.println(
                    "Error while adding citizen."
            );

            e.printStackTrace();
        }
    }


    // ==========================================
    // GET ALL CITIZENS
    // Collections + Generics
    // ==========================================

    public static List<Citizen> getAllCitizens() {

        // Generic Collection
        List<Citizen> citizens =
                new ArrayList<>();

        String sql =
                "SELECT citizen_id, name, email, " +
                        "phone, password, address " +
                        "FROM citizens";

        try {

            Connection con =
                    DatabaseConnection.getConnection();

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ResultSet rs =
                    ps.executeQuery();

            while (rs.next()) {

                // Creating Citizen object
                Citizen citizen =
                        new Citizen(
                                rs.getInt("citizen_id"),
                                rs.getString("name"),
                                rs.getString("email"),
                                rs.getString("phone"),
                                rs.getString("password"),
                                rs.getString("address")
                        );

                // Adding object to Generic List
                citizens.add(citizen);
            }

            rs.close();
            ps.close();

        } catch (Exception e) {

            System.out.println(
                    "Error while loading citizens."
            );

            e.printStackTrace();
        }

        return citizens;
    }


    // ==========================================
    // UPDATE CITIZEN
    // ==========================================

    public static void updateCitizen(
            int citizenId,
            String name,
            String email,
            String phone,
            String address) {

        String sql =
                "UPDATE citizens " +
                        "SET name=?, email=?, phone=?, address=? " +
                        "WHERE citizen_id=?";

        try {

            Connection con =
                    DatabaseConnection.getConnection();

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setString(4, address);
            ps.setInt(5, citizenId);

            ps.executeUpdate();

            ps.close();

            System.out.println(
                    "Citizen Updated Successfully!"
            );

        } catch (Exception e) {

            System.out.println(
                    "Error while updating citizen."
            );

            e.printStackTrace();
        }
    }


    // ==========================================
    // DELETE CITIZEN
    // ==========================================

    public static void deleteCitizen(
            int citizenId) {

        String sql =
                "DELETE FROM citizens " +
                        "WHERE citizen_id=?";

        try {

            Connection con =
                    DatabaseConnection.getConnection();

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setInt(1, citizenId);

            ps.executeUpdate();

            ps.close();

            System.out.println(
                    "Citizen Deleted Successfully!"
            );

        } catch (Exception e) {

            System.out.println(
                    "Error while deleting citizen."
            );

            e.printStackTrace();
        }
    }
}