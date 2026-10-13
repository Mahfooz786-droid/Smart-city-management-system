package com.smartcity.exception;

/**
 * Thrown when a lookup by ID returns no row. Maps to HTTP 404 in the web
 * layer and to a user-friendly JOptionPane in the GUI layer.
 */
public class NotFoundException extends SmartCityException {

    public NotFoundException(String entityName, long id) {
        super(entityName + " with id=" + id + " was not found.");
    }

    public NotFoundException(String message) {
        super(message);
    }
}
