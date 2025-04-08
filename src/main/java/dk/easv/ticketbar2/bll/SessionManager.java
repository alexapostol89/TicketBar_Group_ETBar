package dk.easv.ticketbar2.bll;

public class SessionManager {
    private static int currentUserID;

    public static void setCurrentUserID(int id) {
        currentUserID = id;
    }

    public static int getCurrentUserID() {
        return currentUserID;
    }
}
