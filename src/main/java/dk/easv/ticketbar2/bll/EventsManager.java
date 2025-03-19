package dk.easv.ticketbar2.bll;

import dk.easv.ticketbar2.be.Events;
import dk.easv.ticketbar2.dal.web.EventsDAO;

import java.util.List;

public class EventsManager {

    private final EventsDAO eventsDAO = new EventsDAO();


    public List<Events> getEvents() {
        List<Events> events = eventsDAO.getEvents();
        return events;
    }

}
