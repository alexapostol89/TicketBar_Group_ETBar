package dk.easv.ticketbar2.gui.controllers;

import dk.easv.ticketbar2.be.Events;
import dk.easv.ticketbar2.dal.web.EventsDAO;
import javafx.fxml.FXML;
import dk.easv.ticketbar2.dal.exceptions.EventsException;

import javafx.scene.control.Label;

public class EventDetailsController {

    @FXML
    private Label nameLabel, startDateLabel, endDateLabel, locationLabel, descriptionLabel, coordinatorLabel, guideLabel, notesLabel;
    private EventsDAO eventsDAO = new EventsDAO();

    public void populateEventInfo(int eventID) throws EventsException {

        Events event = eventsDAO.getEventById(eventID);
        if (event != null) {
            nameLabel.setText(event.getEventName());
            startDateLabel.setText(event.getStartDateTime());
            endDateLabel.setText(event.getEndDateTime());
            locationLabel.setText(event.getLocation());
            descriptionLabel.setText(event.getDescription());
            coordinatorLabel.setText(String.valueOf(event.getCoordinatorID()));
            guideLabel.setText(event.getLocationGuide());
            notesLabel.setText(event.getNotes());

        }
    }

}