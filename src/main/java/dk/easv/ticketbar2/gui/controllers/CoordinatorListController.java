package dk.easv.ticketbar2.gui.controllers;

import dk.easv.ticketbar2.be.Events;
import dk.easv.ticketbar2.be.Users;
import dk.easv.ticketbar2.bll.EventsManager;
import dk.easv.ticketbar2.bll.UserEventManager;
import dk.easv.ticketbar2.bll.UsersManager;
import dk.easv.ticketbar2.dal.exceptions.EventsException;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

public class CoordinatorListController {

    @FXML
    private TableView<String> tableView; // TableView to list coordinators

    @FXML
    private TableColumn<String, String> colEvent; // TableColumn for coordinator names

    @FXML
    private Button assignButton; // Button to assign coordinators

    private int eventID; // Holds the event ID passed from the previous screen

    private final UsersManager userManager = new UsersManager();
    private final UserEventManager userEventManager = new UserEventManager(); // New manager for saving UserEvent relations

    @FXML
    public void initialize() {
        colEvent.setCellValueFactory(data -> new SimpleStringProperty(data.getValue())); // Bind data
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE); // Allow multiple selection

        // Assign coordinators when button is clicked
        assignButton.setOnAction(this::assignCoordinators);
    }

    private void loadUserNames() {

        try {
            ObservableList<String> userNames = userManager.getUsersNamesNotAssignedToEvent(eventID);
            tableView.setItems(userNames);
        } catch (EventsException e) {
            e.printStackTrace(); // Handle properly in production
        }
    }

    // Method to assign selected coordinators to the event
    @FXML
    private void assignCoordinators(ActionEvent event) {
        ObservableList<String> selectedUsers = tableView.getSelectionModel().getSelectedItems();

        if (selectedUsers.isEmpty()) {
            showAlert("Selection Error", "No coordinator selected.", Alert.AlertType.WARNING);
            return;
        }

        try {
            for (String userName : selectedUsers) {
                int userID = userManager.getUserIDByName(userName); // Get UserID from Users table
                userEventManager.assignUserToEvent(userID, eventID); // Save in UserEvent table
            }
            showAlert("Success", "Coordinators assigned successfully!", Alert.AlertType.INFORMATION);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        } catch (EventsException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to assign coordinators.", Alert.AlertType.ERROR);
        }

    }

    // Helper method to show alerts
    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Set the event ID when opening the Coordinator List
    public void setEventID(int eventID) {
        this.eventID = eventID;
        System.out.println("Event ID set to: " + this.eventID);
        loadUserNames(); // When the system gets the EventID it load the Users not Assing to that event
    }
}










