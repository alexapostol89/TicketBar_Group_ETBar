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

    public void populateEventInfo(int eventID) throws EventsException {

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
    @FXML
    private void btnAssignCoordinator(ActionEvent event) {
        System.out.println("Assign Coordinator button clicked!");
        openAssignCoordinatorView();
    }

    private void openAssignCoordinatorView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dk/easv/ticketbar2/coordinator-list.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Assign Coordinator");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}