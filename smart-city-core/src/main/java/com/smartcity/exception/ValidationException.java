package com.smartcity.exception;

/**
 * Thrown when user-supplied input fails server-side validation
 * (blank fields, malformed email, malformed phone, etc.).
 */
public class ValidationException extends SmartCityException {

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
