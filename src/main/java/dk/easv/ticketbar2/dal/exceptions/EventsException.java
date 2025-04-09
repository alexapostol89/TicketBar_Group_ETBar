package dk.easv.ticketbar2.dal.exceptions;

/*public class EventsException extends Exception {

    public EventsException(SQLException cause) {
        super(cause);
    }

    // New constructor accepting both a message and SQLException
    public EventsException(String message, SQLException cause) {
        super(message, cause);
    }

    public EventsException(String s) {
    }*/


import java.sql.SQLException;

//DAVID METHOD'S
    public class EventsException extends RuntimeException {
        public EventsException(String message) {
            super(message);
        }
        public EventsException(Exception e) { super(e); }
        public EventsException(String s, SQLException e) {
            super(s, e);
        }
    }


