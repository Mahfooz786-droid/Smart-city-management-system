package com.smartcity.service;

import com.smartcity.exception.ValidationException;
import com.smartcity.model.Citizen;

/**
 * Stateless input validation helpers shared by the service layer.
 *
 * <p>Centralising validation here keeps the service methods readable and
 * guarantees identical rules in both the GUI and the Web module.
 */
public final class Validator {

    private Validator() {}

    public static void requireNonBlank(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException(fieldName + " is required.");
        }
    }

    public static void requireEmail(String email) {
        if (email == null || !email.contains("@") || !email.contains(".")) {
            throw new ValidationException("Please enter a valid email.");
        }
    }

    /** 10-digit Indian mobile numbers. */
    public static void requirePhone(String phone) {
        if (phone == null || !phone.matches("\\d{10}")) {
            throw new ValidationException("Phone number must contain exactly 10 digits.");
        }
    }

    /** Rating range 1..5 inclusive. */
    public static void requireRating(int rating) {
        if (rating < 1 || rating > 5) {
            throw new ValidationException("Rating must be between 1 and 5.");
        }
    }

    /** Positive integer ID. */
    public static void requirePositiveId(int id, String fieldName) {
        if (id <= 0) {
            throw new ValidationException(fieldName + " must be greater than 0.");
        }
    }

    /**
     * Validates every field on a {@link Citizen} that the persistence layer
     * requires. Used by both {@code save} and {@code update} flows so the
     * rules cannot drift.
     */
    public static void validateCitizenFields(String name, String email, String phone, String address) {
        requireNonBlank(name, "Name");
        requireEmail(email);
        requirePhone(phone);
        requireNonBlank(address, "Address");
    }
}
