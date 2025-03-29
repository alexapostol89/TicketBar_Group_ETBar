package dk.easv.ticketbar2.bll;

import dk.easv.ticketbar2.dal.exceptions.EventsException;
import dk.easv.ticketbar2.dal.web.UserEventDAO;

public class UserEventManager {


    private final UserEventDAO userEventDAO = new UserEventDAO();

    public void assignUserToEvent(int userID, int eventID) throws EventsException {
        userEventDAO.insertUserEvent(userID, eventID);
    }
}

