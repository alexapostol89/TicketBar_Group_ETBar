package dk.easv.ticketbar2.dal.exceptions;

public class UsersException extends RuntimeException {

    public UsersException(String message) {
        super(message);
    }
    public UsersException(Exception e) { super(e); }
}