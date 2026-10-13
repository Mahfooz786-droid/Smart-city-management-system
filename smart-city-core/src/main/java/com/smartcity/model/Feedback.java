package com.smartcity.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * A piece of feedback submitted by a {@link Citizen} about a city service
 * or the platform in general. {@code rating} is constrained to the range
 * 1-5 (inclusive) by the service layer.
 */
public class Feedback {

    private int id;
    private int citizenId;
    private String citizenName;
    private int rating;
    private String comment;
    private LocalDateTime createdAt;

    public Feedback() {}

    public Feedback(int id, int citizenId, String citizenName, int rating, String comment, LocalDateTime createdAt) {
        this.id = id;
        this.citizenId = citizenId;
        this.citizenName = citizenName;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCitizenId() { return citizenId; }
    public void setCitizenId(int citizenId) { this.citizenId = citizenId; }

    public String getCitizenName() { return citizenName; }
    public void setCitizenName(String citizenName) { this.citizenName = citizenName; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Feedback f)) return false;
        return id == f.id;
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public String toString() {
        return "Feedback{id=" + id + ", citizenId=" + citizenId +
                ", rating=" + rating + ", comment='" + comment + "'}";
    }
}
