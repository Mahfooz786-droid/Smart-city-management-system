package com.smartcity.database;

import com.smartcity.dao.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ServiceDAO {

    // Get all city services
    public static List<String> getAllServices() {

        List<String> services = new ArrayList<>();

        String sql =
                "SELECT service_id, service_name, " +
                        "description, department, status " +
                        "FROM services " +
                        "ORDER BY service_id";

        try {

            Connection con =
                    DatabaseConnection.getConnection();

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ResultSet rs =
                    ps.executeQuery();

            while (rs.next()) {

                String service =
                        "Service ID: "
                                + rs.getInt("service_id")
                                + "\nService: "
                                + rs.getString("service_name")
                                + "\nDescription: "
                                + rs.getString("description")
                                + "\nDepartment: "
                                + rs.getString("department")
                                + "\nStatus: "
                                + rs.getString("status");

                services.add(service);
            }

            rs.close();
            ps.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return services;
    }
}