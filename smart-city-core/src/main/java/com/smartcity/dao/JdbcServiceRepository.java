package com.smartcity.dao;

import com.smartcity.config.DatabaseConfig;
import com.smartcity.exception.DatabaseException;
import com.smartcity.interfaces.ServiceRepository;
import com.smartcity.model.CityService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of {@link ServiceRepository}.
 *
 * <p>Service catalog rows are read-only in the current scope; the schema
 * is initialised from {@code database/seed.sql}.
 */
public class JdbcServiceRepository implements ServiceRepository {

    private final DatabaseConfig db;

    public JdbcServiceRepository(DatabaseConfig db) {
        this.db = db;
    }

    @Override
    public List<CityService> findAll() {
        final String sql = "SELECT service_id, service_name, description, department, status " +
                           "FROM services ORDER BY service_id ASC";
        List<CityService> out = new ArrayList<>();
        try (Connection con = db.openConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(new CityService(
                        rs.getInt("service_id"),
                        rs.getString("service_name"),
                        rs.getString("description"),
                        rs.getString("department"),
                        CityService.Availability.fromDb(rs.getString("status"))
                ));
            }
            return out;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to load city services.", e);
        }
    }
}
