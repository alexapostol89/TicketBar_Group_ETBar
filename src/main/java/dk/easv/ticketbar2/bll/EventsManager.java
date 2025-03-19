package dk.easv.ticketbar2.bll;

import dk.easv.ticketbar2.be.Events;
import dk.easv.ticketbar2.dal.web.EventsDAO;
import javafx.collections.ObservableList;

public class EventsManager {
    private EventsDAO eventsDAO = new EventsDAO();

    // Get all events
    public ObservableList<Events> getAllEvents() {
        return eventsDAO.getAllEvents();
    }

    // Get event by ID
    public Events getEventById(int eventId) {
        return eventsDAO.getEventById(eventId);
    }
}
