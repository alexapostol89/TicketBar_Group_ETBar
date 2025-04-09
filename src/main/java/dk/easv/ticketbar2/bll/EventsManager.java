package dk.easv.ticketbar2.bll;

import dk.easv.ticketbar2.be.Events;
import dk.easv.ticketbar2.dal.exceptions.EventsException;
import dk.easv.ticketbar2.dal.web.EventsDAO;
import javafx.collections.ObservableList;

import java.util.List;

public class EventsManager {

    private final EventsDAO eventsDAO = new EventsDAO();

    public List<Events> getEvents() throws EventsException {
        List<Events> events = eventsDAO.getAllEvents();
        return events;
    }

   // public Events getEventById(int eventId) throws EventsException {
    //    return eventsDAO.getEventById(eventId);
    //}

    public int saveEvent(String eventName, String imagePath, String startDate, String endDate,
                         String location, String description, String locationGuide,
                         String notes) throws EventsException {
        return eventsDAO.saveEvent(eventName, imagePath, startDate, endDate, location,
                description, locationGuide, notes);
    }

    public boolean deleteEvent(int eventID) throws EventsException {
        return eventsDAO.deleteEvent(eventID);
    }

    public boolean updateEvent(Events event) {
        return eventsDAO.updateEvent(event);
    }

    //DAVID METHOD'S

    // Get all events
    public ObservableList<Events> getAllEvents() {
        return eventsDAO.getAllEvents();
    }

    // Get event by ID
    public Events getEventById(int eventId) {
        return eventsDAO.getEventById(eventId);
    }

    // Get the coordinator's username by their UserID
    public String getCoordinatorUsername(int coordinatorID) {
        return eventsDAO.getCoordinatorUsername(coordinatorID);
    }

    // Get the coordinator's First name and Lastname as one
    public String getCoordinatorFullName(int coordinatorID) {
        return eventsDAO.getCoordinatorFullName(coordinatorID);
    }

    // Method to update the event's coordinator
    public void updateEventCoordinator(Events event) {
        eventsDAO.updateEventCoordinator(event);
    }

    // Method to delete an event
    public void deleteEvent(Events event) {
        eventsDAO.deleteEvent(event);
    }
}








