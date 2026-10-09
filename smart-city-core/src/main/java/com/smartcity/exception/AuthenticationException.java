package com.smartcity.exception;

/**
 * Thrown when an authentication attempt fails (wrong email / wrong password,
 * unknown account, etc.).
 *
 * <p>The message is intentionally generic ("Invalid email or password.") so
 * it can be safely echoed back to end users without leaking which field was
 * wrong (defence against account enumeration).
 */
public class AuthenticationException extends SmartCityException {

    /** Generic, non-enumerable auth failure message. */
    public static final String GENERIC_MESSAGE = "Invalid email or password.";

    public AuthenticationException() {
        super(GENERIC_MESSAGE);
    }

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
