package com.smartcity.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * A complaint filed by a {@link Citizen}.
 *
 * <p>Status values are constrained to {@link Status} for type safety. The
 * legacy DAO returned formatted strings; we now keep the typed
 * {@code Complaint} object so the GUI / JSP layer can render columns
 * directly without re-parsing.
 */
public class Complaint {

    public enum Status {
        PENDING("Pending"),
        IN_PROGRESS("In Progress"),
        RESOLVED("Resolved");

        private final String dbValue;
        Status(String dbValue) { this.dbValue = dbValue; }
        public String dbValue() { return dbValue; }

        public static Status fromDb(String raw) {
            for (Status s : values()) {
                if (s.dbValue.equalsIgnoreCase(raw)) return s;
            }
            throw new IllegalArgumentException("Unknown complaint status: " + raw);
        }
    }

    private int id;
    private int citizenId;
    private String citizenName; // denormalised for admin views
    private String subject;
    private String description;
    private Status status;
    private LocalDateTime createdAt;

    public Complaint() {}

    public Complaint(int id, int citizenId, String citizenName, String subject, String description, Status status, LocalDateTime createdAt) {
        this.id = id;
        this.citizenId = citizenId;
        this.citizenName = citizenName;
        this.subject = subject;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCitizenId() { return citizenId; }
    public void setCitizenId(int citizenId) { this.citizenId = citizenId; }

    public String getCitizenName() { return citizenName; }
    public void setCitizenName(String citizenName) { this.citizenName = citizenName; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Complaint c)) return false;
        return id == c.id;
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public String toString() {
        return "Complaint{id=" + id + ", citizenId=" + citizenId +
                ", subject='" + subject + "', status=" + status + '}';
    }
}
