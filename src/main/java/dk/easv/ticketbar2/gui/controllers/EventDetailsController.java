package dk.easv.ticketbar2.gui.controllers;

import dk.easv.ticketbar2.be.Events;
import dk.easv.ticketbar2.be.Tickets;
import dk.easv.ticketbar2.bll.EventsManager;
import dk.easv.ticketbar2.dal.web.EventsDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import dk.easv.ticketbar2.dal.exceptions.EventsException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class EventDetailsController {

    @FXML
    private Button deleteBtn, editEvents, addVoucher;

    @FXML
    private Label nameLabel, startDateLabel, endDateLabel, locationLabel, descriptionLabel, coordinatorLabel, guideLabel, notesLabel;

    private final EventsManager eventsManager = new EventsManager();

    private CoordinatorController coordinatorController;

    private int eventID; // Store the current event ID

    public void setCoordinatorController(CoordinatorController controller) {
        this.coordinatorController = controller;
    }

    @FXML
    private void initialize() {
        editEvents.setOnAction(event ->
            openAddEditEventWindow(eventID));

    }


    // Method to delete the selected event from the app and database
    @FXML
    private void onActionDelete(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("Are you sure you want to delete this event?");
        alert.setContentText("This action cannot be undone.");

        Optional<ButtonType> result = alert.showAndWait();
        boolean success = false;
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                success = eventsManager.deleteEvent(eventID);
                if (success) {
                    System.out.println("Event deleted successfully.");

                    // Close current window
                    Stage stage = (Stage) deleteBtn.getScene().getWindow();
                    stage.close();
                } else {
                    System.out.println("Failed to delete the event.");
                }
            } catch (EventsException e) {
                e.printStackTrace();
            }
        }
        if (success) {
            System.out.println("Event deleted successfully.");

            // Refresh the coordinator view if reference is set
            if (coordinatorController != null) {
                coordinatorController.refreshEvents();
            }

            // Close the current window
            Stage stage = (Stage) deleteBtn.getScene().getWindow();
            stage.close();
        }
    }

    public void populateEventInfo(int eventID) throws EventsException {
        this.eventID = eventID; // Store the event ID

        Events event = eventsManager.getEventById(eventID);
        String fullName = eventsManager.getCoordinatorFullName(event.getCoordinatorID());


        if (event != null) {
            nameLabel.setText(event.getEventName());
            startDateLabel.setText(event.getStartDateTime());
            endDateLabel.setText(event.getEndDateTime());
            locationLabel.setText(event.getLocation());
            descriptionLabel.setText(event.getDescription());
            coordinatorLabel.setText(fullName != null ? fullName : "Coordinator not assigned");
            guideLabel.setText(event.getLocationGuide());
            notesLabel.setText(event.getNotes());

        }
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
            stage.setTitle("Assign or Unassign Coordinators");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void handlePrintTickets(ActionEvent event) {
        System.out.println("Print Ticket button clicked!");
        System.out.println("Event ID in CoordinatorController before passing: " + eventID);  // Debugging line

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dk/easv/ticketbar2/print-ticket.fxml"));
            Parent root = loader.load();

            // Get the SaveTicketsController instance after loading the FXML
            SaveTicketsController saveTicketsController = loader.getController();

            // Pass the eventID to the SaveTicketsController
            saveTicketsController.setEventID(this.eventID);  // This ensures eventID is set before loading details

            System.out.println("eventID in printTicket: " + this.eventID); // Debugging line

            Stage stage = new Stage();
            stage.setTitle("Print Ticket");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openAddVoucherWindow(ActionEvent event) throws EventsException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dk/easv/ticketbar2/voucher-dialog.fxml"));
            Scene scene = new Scene(loader.load());

            // Get the SaveTicketsController instance after loading the FXML
            SaveVouchersController saveVouchersController = loader.getController();

            // Pass the eventID to the SaveTicketsController
            saveVouchersController.setEventID(this.eventID);  // This ensures eventID is set before loading details

            System.out.println("eventID in printTicket: " + this.eventID);

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Add Voucher");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (EventsException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void sendTicketsByEmail(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Email Status");
        alert.setHeaderText(null);
        alert.setContentText("Email sent");

        alert.showAndWait();

    }


    // Setter for eventID (for setting eventID in this controller)
    public void setEventID(int eventID) {
        this.eventID = eventID;
        System.out.println("Event ID set in CoordinatorController: " + eventID);  // Debugging line
    }
    public int getEventID() {
        return eventID;
    }

    private void openAddEditEventWindow(Integer eventID) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dk/easv/ticketbar2/add-event.fxml"));
            Parent root = loader.load();
            AddEventsController editEventsController = loader.getController();

            // Pass CoordinatorController to refresh events
            if (coordinatorController != null) {
                editEventsController.setCoordinatorController(coordinatorController);
            }

            if (eventID != null) {
                Events eventToEdit = eventsManager.getEventById(eventID);
                editEventsController.setEventToEdit(eventToEdit);
            }

            Stage stage = new Stage();
            stage.setTitle(eventID != null ? "Edit Event" : "Add Event");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException | EventsException e) {
            e.printStackTrace();
        }
    }
}
