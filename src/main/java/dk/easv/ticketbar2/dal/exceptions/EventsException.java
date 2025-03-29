package dk.easv.ticketbar2.dal.exceptions;

import java.sql.SQLException;

public class EventsException extends Exception {

    public EventsException(SQLException cause) {
        super(cause);
    }

    // New constructor accepting both a message and SQLException
    public EventsException(String message, SQLException cause) {
        super(message, cause);
    }

    public EventsException(String s) {
    }
}

