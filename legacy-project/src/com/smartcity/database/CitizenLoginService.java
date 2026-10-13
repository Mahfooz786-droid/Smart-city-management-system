package com.smartcity.database;

import com.smartcity.dao.DatabaseConnection;
import com.smartcity.model.Citizen;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CitizenLoginService {

    public static Citizen loginCitizen(
            String email,
            String password) {

        String sql =
                "SELECT citizen_id, name, email, phone, password, address " +
                        "FROM citizens " +
                        "WHERE email = ? AND password = ?";

        try {

            Connection con =
                    DatabaseConnection.getConnection();

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs =
                    ps.executeQuery();

            if (rs.next()) {

                Citizen citizen = new Citizen(
                        rs.getInt("citizen_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("password"),
                        rs.getString("address")
                );

                rs.close();
                ps.close();

                return citizen;
            }

            rs.close();
            ps.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}