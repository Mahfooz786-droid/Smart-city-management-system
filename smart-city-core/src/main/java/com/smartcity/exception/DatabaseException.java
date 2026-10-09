package com.smartcity.exception;

/**
 * Thrown when the underlying database is unreachable or a SQL operation
 * fails. Wraps the original {@link java.sql.SQLException} so the caller can
 * decide whether to display a generic error to the user or rethrow.
 *
 * <p>This replaces the previous pattern of swallowing SQLExceptions and
 * returning a misleading "success" value.
 */
public class DatabaseException extends SmartCityException {

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public DatabaseException(String message) {
        super(message);
    }
}
