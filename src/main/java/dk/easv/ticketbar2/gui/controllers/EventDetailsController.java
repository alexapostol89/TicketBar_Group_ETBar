package dk.easv.ticketbar2.gui.controllers;

import dk.easv.ticketbar2.be.Events;
import dk.easv.ticketbar2.bll.EventsManager;
import dk.easv.ticketbar2.dal.web.EventsDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import dk.easv.ticketbar2.dal.exceptions.EventsException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class EventDetailsController {

    @FXML
    private Label nameLabel, startDateLabel, endDateLabel, locationLabel, descriptionLabel, coordinatorLabel, guideLabel, notesLabel;
    private final EventsManager eventsManager = new EventsManager();

    private int eventID; // Store the current event ID

    public void populateEventInfo(int eventID) throws EventsException {
        this.eventID = eventID; // Store the event ID

        Events event = eventsManager.getEventById(eventID);

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

    public int getEventID() {  // Add this method
        return this.eventID;
    }

    @FXML
    private void btnAssignCoordinator(ActionEvent event) {

        openCoordinatorList(eventID); // Now eventID is properly stored
    }

    private void openCoordinatorList(int eventID) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dk/easv/ticketbar2/coordinator-list.fxml"));
            Parent root = loader.load();

            CoordinatorListController controller = loader.getController();
            controller.setEventID(eventID); // Pass event ID

            Stage stage = new Stage();
            stage.setTitle("Assign Coordinators");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}