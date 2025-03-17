package dk.easv.ticketbar2.dal.exceptions;

public class UserException extends RuntimeException {
    public UserException(Exception e) {super(e);}
    public UserException(String message) {
        super(message);
    }
}
