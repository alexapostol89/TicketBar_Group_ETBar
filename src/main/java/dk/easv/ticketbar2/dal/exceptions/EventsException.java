package dk.easv.ticketbar2.dal.exceptions;

public class EventsException extends RuntimeException {
  public EventsException(String message) {
        super(message);
    }
    public EventsException(Exception e) { super(e); }
}
