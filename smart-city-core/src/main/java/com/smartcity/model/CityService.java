package com.smartcity.model;

import java.util.Objects;

/**
 * A city service offered by the municipality (water supply, garbage
 * collection, etc.). Catalog rows are managed via {@code database/seed.sql}.
 */
public class CityService {

    public enum Availability {
        ACTIVE("Active"),
        MAINTENANCE("Maintenance"),
        DEPRECATED("Deprecated");

        private final String dbValue;
        Availability(String v) { this.dbValue = v; }
        public String dbValue() { return dbValue; }

        public static Availability fromDb(String raw) {
            for (Availability a : values()) {
                if (a.dbValue.equalsIgnoreCase(raw)) return a;
            }
            return ACTIVE;
        }
    }

    private int id;
    private String name;
    private String description;
    private String department;
    private Availability status;

    public CityService() {}

    public CityService(int id, String name, String description, String department, Availability status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.department = department;
        this.status = status;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public Availability getStatus() { return status; }
    public void setStatus(Availability status) { this.status = status; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CityService s)) return false;
        return id == s.id;
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public String toString() {
        return "CityService{id=" + id + ", name='" + name + "', department='" + department + "'}";
    }
}
