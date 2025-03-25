package dk.easv.ticketbar2.bll;

import dk.easv.ticketbar2.be.Event;
import dk.easv.ticketbar2.dal.web.EventDAO;
import javafx.collections.ObservableList;

public class EventManager {
    private EventDAO eventDAO = new EventDAO();

    // Get all events
    public ObservableList<Event> getAllEvents() {
        return eventDAO.getAllEvents();
    }

    // Get event by ID
    public Event getEventById(int eventId) {
        return eventDAO.getEventById(eventId);
    }

    // Get the coordinator's username by their UserID
    public String getCoordinatorUsername(int coordinatorID) {
        return eventDAO.getCoordinatorUsername(coordinatorID);
    }

    // Method to update the event's coordinator
    public void updateEventCoordinator(Event event) {
        eventDAO.updateEventCoordinator(event);
    }

    // Method to delete an event
    public void deleteEvent(Event event) {
        eventDAO.deleteEvent(event);
    }
}
