package fr.kiza.leagueuhc.core.database.exception;

public class DatabaseException extends RuntimeException {
    public DatabaseException(final String message) {
        super(message);
    }

    public DatabaseException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
