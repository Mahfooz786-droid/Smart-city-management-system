package com.smartcity.exception;

/**
 * Base class for all custom Smart City application exceptions.
 *
 * <p>All application-specific failures extend this so callers can catch the
 * family in one place (e.g. {@code catch (SmartCityException e)}) while still
 * being able to differentiate between auth failures, validation failures,
 * not-found errors, etc. via the subclasses.
 */
public abstract class SmartCityException extends RuntimeException {

    protected SmartCityException(String message) {
        super(message);
    }

    protected SmartCityException(String message, Throwable cause) {
        super(message, cause);
    }
}
