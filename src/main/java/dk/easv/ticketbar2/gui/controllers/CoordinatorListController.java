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

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

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
            ObservableList<String> assignedUsers = userManager.getUsersNamesAssignedToEvent(eventID);
            ObservableList<String> unassignedUsers = userManager.getUsersNamesNotAssignedToEvent(eventID);

            Set<String> allUsersSet = new LinkedHashSet<>();

            // Modify the display for assigned users
            Map<String, String> displayMap = new HashMap<>();
            for (String user : assignedUsers) {
                String displayText = user + " (Assigned)";
                allUsersSet.add(displayText);
                displayMap.put(displayText, user); // Store original name for selection
            }

            allUsersSet.addAll(unassignedUsers);
            ObservableList<String> allUsers = FXCollections.observableArrayList(allUsersSet);
            tableView.setItems(allUsers);

            // Clear selection first
            tableView.getSelectionModel().clearSelection();

            // Select only assigned coordinators
            for (String assignedUser : assignedUsers) {
                String displayText = assignedUser + " (Assigned)";
                int index = allUsers.indexOf(displayText);
                if (index >= 0) {
                    tableView.getSelectionModel().select(index);
                }
            }

        } catch (EventsException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void assignCoordinators(ActionEvent event) {
        ObservableList<String> selectedUsers = tableView.getSelectionModel().getSelectedItems();

        try {
            // Get currently assigned users
            ObservableList<String> assignedUsersBefore = userManager.getUsersNamesAssignedToEvent(eventID);

            // Determine newly assigned users
            for (String userName : selectedUsers) {
                if (!assignedUsersBefore.contains(userName)) {
                    int userID = userManager.getUserIDByName(userName);
                    userEventManager.assignUserToEvent(userID, eventID);
                }
            }

            // Determine users to unassign
            for (String userName : assignedUsersBefore) {
                if (!selectedUsers.contains(userName)) {
                    int userID = userManager.getUserIDByName(userName);
                    userEventManager.unassignUserFromEvent(userID, eventID);
                }
            }

            showAlert("Success", "Coordinator assignments updated!", Alert.AlertType.INFORMATION);
            loadUserNames(); // Refresh the list after assignment

        } catch (EventsException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to update coordinator assignments.", Alert.AlertType.ERROR);
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










