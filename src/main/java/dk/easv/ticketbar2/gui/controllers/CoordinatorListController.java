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

import java.util.*;

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

            // Add unassigned users without modifying their display text
            for (String user : unassignedUsers) {
                allUsersSet.add(user);
                displayMap.put(user, user); // Store original name for selection
            }

            ObservableList<String> allUsers = FXCollections.observableArrayList(allUsersSet);
            tableView.setItems(allUsers);

            // Clear selection first
            tableView.getSelectionModel().clearSelection();

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

            // Create sets for easier comparison
            Set<String> assignedUserNamesBefore = new HashSet<>(assignedUsersBefore);

            // Process selected users
            for (String displayText : selectedUsers) {
                if (displayText.endsWith(" (Assigned)")) {
                    // Unassign user with warning
                    String userName = displayText.replace(" (Assigned)", "");
                    int userID = userManager.getUserIDByName(userName);
                    if (showUnassignWarning(userName)) {
                        userEventManager.unassignUserFromEvent(userID, eventID);
                    }
                } else {
                    // Assign user
                    String userName = displayText;
                    if (!assignedUserNamesBefore.contains(userName)) {
                        int userID = userManager.getUserIDByName(userName);
                        userEventManager.assignUserToEvent(userID, eventID);
                    }
                }
            }

            showAlert("Success", "Coordinator assignments updated!", Alert.AlertType.INFORMATION);
            loadUserNames(); // Refresh the list after assignment

        } catch (EventsException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to update coordinator assignments.", Alert.AlertType.ERROR);
        }
    }

    // Helper method to show unassign warning
    private boolean showUnassignWarning(String userName) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Unassign Coordinator");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to unassign " + userName + " as a coordinator?");

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
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










