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
    public Events getEventById(int eventId) throws EventsException {
        return eventsDAO.getEventById(eventId);
    }

    public int saveEvent(String eventName, String imagePath, String startDate, String endDate,
                         String location, String description, String locationGuide,
                         String notes, int coordinatorId) throws EventsException {
        return eventsDAO.saveEvent(eventName, imagePath, startDate, endDate, location,
                description, locationGuide, notes, coordinatorId);
    }
    public boolean deleteEvent(int eventID) throws EventsException {
        return eventsDAO.deleteEvent(eventID);
    }
}

