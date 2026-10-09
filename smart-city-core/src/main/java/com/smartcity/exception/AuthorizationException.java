package com.smartcity.exception;

/**
 * Thrown when an authenticated principal attempts an action they are not
 * authorised to perform (e.g. a citizen trying to delete another citizen's
 * complaint).
 */
public class AuthorizationException extends SmartCityException {

    public AuthorizationException(String message) {
        super(message);
    }
}
