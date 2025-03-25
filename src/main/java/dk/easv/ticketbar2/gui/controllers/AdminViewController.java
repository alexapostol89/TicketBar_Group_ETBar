package dk.easv.ticketbar2.gui.controllers;

import dk.easv.ticketbar2.be.Event;
import dk.easv.ticketbar2.be.User;
import dk.easv.ticketbar2.bll.EventManager;
import dk.easv.ticketbar2.bll.UserManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Optional;

public class AdminViewController {
    @FXML
    private TableView<User> usersTableView;

    @FXML
    private TableColumn<User, String> firstNameColumn;

    @FXML
    private TableColumn<User, String> usernameColumn;

    @FXML
    private TableColumn<User, String> rankColumn;

    @FXML
    private Label firstNameLabel, lastNameLabel, rankLabel, usernameLabel, emailLabel, phoneLabel, dateCreatedLabel, lastLoginLabel;

    @FXML
    private TableView<Event> eventsTableView;

    @FXML
    private TableColumn<Event, String> nameColumn;

    @FXML
    private TableColumn<Event, String> dateColumn;

    @FXML
    private TableColumn<Event, String> coordinatorColumn;

    @FXML
    private Label nameLabel, startDateLabel, endDateLabel, locationLabel, descriptionLabel, coordinatorLabel, guideLabel, notesLabel;

    @FXML
    private Button signOutButton1, signOutButton2, addUserButton, editUserButton, deleteUserButton, assignCoordinatorButton, deleteEventButton;

    @FXML
    private CheckBox adminCheckBox, coordinatorCheckBox, missingCoordinatorCheckBox;

    @FXML
    private TextField usernameTextField, eventNameTextField;

    private UserManager userManager = new UserManager();
    private EventManager eventManager = new EventManager();

    public void initialize() {
        // Setup Users TableView
        firstNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName()));
        usernameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));
        rankColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRankName()));
        usersTableView.setItems(userManager.getAllUsers());

        // Setup Events TableView
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEventName()));
        dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStartDateTime()));
        // coordinatorColumn.setCellValueFactory(cellData -> new SimpleStringProperty(Integer.toString(cellData.getValue().getCoordinatorID())));

        // Modify the cell value factory for the Coordinator column
        coordinatorColumn.setCellValueFactory(cellData -> {
            Event event = cellData.getValue();
            // Check if the event has a coordinator (CoordinatorID != 0)
            if (event.getCoordinatorID() == 0) {
                return new SimpleStringProperty("Not Assigned");
            } else {
                // Get the coordinator's username by CoordinatorID
                String coordinatorUsername = eventManager.getCoordinatorUsername(event.getCoordinatorID());
                return new SimpleStringProperty(coordinatorUsername);
            }
        });

        eventsTableView.setItems(eventManager.getAllEvents());

        // Add listeners to populate details when a row is selected
        usersTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                populateUserDetails(newValue);
            }
        });

        eventsTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                populateEventDetails(newValue);
            }
        });

        // Add listeners to checkboxes and text field for filtering
        adminCheckBox.setOnAction(e -> filterUsers());
        coordinatorCheckBox.setOnAction(e -> filterUsers());
        usernameTextField.textProperty().addListener((observable, oldValue, newValue) -> filterUsers());

        missingCoordinatorCheckBox.setOnAction(e -> filterEvents());
        eventNameTextField.textProperty().addListener((observable, oldValue, newValue) -> filterEvents());

        // 'Sign out' button action
        signOutButton1.setOnAction(this::signOut);
        signOutButton2.setOnAction(this::signOut);

        // 'Add User' button action
        addUserButton.setOnAction(this::handleAddUser);

        // 'Edit User' button action
        editUserButton.setOnAction(this::handleEditUser);

        // 'Delete User' button action
        deleteUserButton.setOnAction(this::handleDeleteUser);

        // 'Assign Coordinator' button action
        assignCoordinatorButton.setOnAction(this::handleAssignCoordinator);

        // 'Delete Event' button action
        deleteEventButton.setOnAction(this::handleDeleteEvent);
    }

    private void populateUserDetails(User user) {
        firstNameLabel.setText(user.getFirstName());
        lastNameLabel.setText(user.getLastName());
        rankLabel.setText(user.getRankName());
        usernameLabel.setText(user.getUsername());
        emailLabel.setText(user.getEmail());
        phoneLabel.setText(user.getPhone());
        dateCreatedLabel.setText(user.getCreatedDate());
        lastLoginLabel.setText(user.getLastLogin());
    }

    private void populateEventDetails(Event event) {
        nameLabel.setText(event.getEventName());
        startDateLabel.setText(event.getStartDateTime());
        endDateLabel.setText(event.getEndDateTime());
        locationLabel.setText(event.getLocation());
        descriptionLabel.setText(event.getDescription());

        // Fetch the coordinator's username and display it
        String coordinatorUsername = event.getCoordinatorID() == 0
                ? "Not Assigned"
                : eventManager.getCoordinatorUsername(event.getCoordinatorID());

        coordinatorLabel.setText(coordinatorUsername);
        guideLabel.setText(event.getLocationGuide());
        notesLabel.setText(event.getNotes());
    }

    private void handleAssignCoordinator(ActionEvent event) {
        // Get the selected event from the TableView
        Event selectedEvent = eventsTableView.getSelectionModel().getSelectedItem();

        // Check if an event is selected
        if (selectedEvent == null) {
            showErrorDialog("Error", "Please select an event");
            return;
        }

        // Check if the selected event already has a coordinator assigned
        if (selectedEvent.getCoordinatorID() != 0) {
            showErrorDialog("Error", "A coordinator is already assigned to this event");
            return;
        }

        // Open the ListView dialog to select a coordinator
        openAssignCoordinatorDialog(selectedEvent);
    }

    private void openAssignCoordinatorDialog(Event selectedEvent) {
        // Create a new stage for the dialog
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);  // Make sure it's modal
        dialogStage.setTitle("Assign Coordinator");

        // Create a ListView for the coordinators
        ListView<String> coordinatorListView = new ListView<>();

        // Fetch all users with the "Coordinator" role
        ObservableList<String> coordinatorUsernames = FXCollections.observableArrayList();
        for (User user : userManager.getAllUsers()) {
            if (user.getRankName().equals("Coordinator")) {
                coordinatorUsernames.add(user.getUsername());
            }
        }

        coordinatorListView.setItems(coordinatorUsernames);

        // Confirm button action
        Button confirmButton = new Button("Confirm");
        confirmButton.setOnAction(e -> {
            String selectedCoordinatorUsername = coordinatorListView.getSelectionModel().getSelectedItem();

            if (selectedCoordinatorUsername != null) {
                // Find user by Username (i.e. fetch the UserID from the selected coordinator's username)
                User selectedCoordinator = null;
                for (User user : userManager.getAllUsers()) {
                    if (user.getUsername().equals(selectedCoordinatorUsername)) {
                        selectedCoordinator = user;
                        break;
                    }
                }

                if (selectedCoordinator != null) {
                    // Update the selected event coordinator
                    selectedEvent.setCoordinatorID(selectedCoordinator.getUserID());

                    // Update the event
                    eventManager.updateEventCoordinator(selectedEvent);

                    // Close the dialog window and refresh the table view
                    dialogStage.close();
                    eventsTableView.refresh();
                } else {
                    showErrorDialog("Error","Coordinator not found.");
                }
            } else {
                showErrorDialog("Error","Please select a coordinator.");
            }
        });

        // Layout for the dialog window (ListView + Confirm button)
        VBox vbox = new VBox(10, coordinatorListView, confirmButton);
        vbox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vbox, 300, 400);
        dialogStage.setScene(scene);
        dialogStage.show();
    }

    // Method to handle Add User button
    private void handleAddUser(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dk/easv/ticketbar2/add-edit-view.fxml"));
            Parent root = loader.load();

            // Get the controller of the dialog
            AddEditUserController dialogController = loader.getController();
            dialogController.setDialogType("add");

            // Open the dialog
            Stage dialogStage = new Stage();
            dialogStage.setScene(new Scene(root));
            dialogStage.setTitle("Create New User");
            dialogStage.showAndWait();

            // After the dialog closes, refresh the Users TableView
            usersTableView.setItems(FXCollections.observableArrayList(userManager.getAllUsers()));

        } catch (Exception e) {
            showErrorDialog("Error", "Failed to open the Add User dialog");
        }
    }

    // Method to handle Edit User button
    private void handleEditUser(ActionEvent event) {
        User selectedUser = usersTableView.getSelectionModel().getSelectedItem();

        if (selectedUser != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/dk/easv/ticketbar2/add-edit-view.fxml"));
                Parent root = loader.load();

                // Get the controller of the dialog
                AddEditUserController dialogController = loader.getController();
                dialogController.setDialogType("edit");
                dialogController.setUser(selectedUser);

                // Open the dialog
                Stage dialogStage = new Stage();
                dialogStage.setScene(new Scene(root));
                dialogStage.setTitle("Edit User");
                dialogStage.showAndWait();

                // After the dialog closes, refresh the Users TableView
                usersTableView.setItems(FXCollections.observableArrayList(userManager.getAllUsers()));

            } catch (Exception e) {
                showErrorDialog("Error", "Failed to open the Edit User dialog");
            }
        } else {
            showErrorDialog("Error", "Please select a user to edit");
        }
    }

    // Method to handle Delete User button
    private void handleDeleteUser(ActionEvent event) {
        // Get the selected user from the table
        User selectedUser = usersTableView.getSelectionModel().getSelectedItem();

        if (selectedUser != null) {
            // Show a confirmation dialog
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirm Deletion");
            confirmationAlert.setHeaderText("Are you sure you want to delete the selected user?");
            confirmationAlert.setContentText("This action cannot be undone");

            // Wait for the user's response (Yes/No)
            ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
            ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
            confirmationAlert.getButtonTypes().setAll(yesButton, noButton);

            // Show the alert and capture the user's choice
            Optional<ButtonType> result = confirmationAlert.showAndWait();

            if (result.isPresent() && result.get() == yesButton) {
                // If the user clicks "Yes", delete the user
                userManager.deleteUser(selectedUser);
                // Remove the user from the TableView
                usersTableView.getItems().remove(selectedUser);
            }
        } else {
            // Show an error if no user is selected
            showErrorDialog("Error", "Please select a user to delete");
        }
    }

    // Method to handle Delete Event button
    private void handleDeleteEvent(ActionEvent event) {
        Event selectedEvent = eventsTableView.getSelectionModel().getSelectedItem();

        if (selectedEvent == null) {
            // Show an error message if no event is selected
            showErrorDialog("Error","Please select an event to delete");
            return;
        }

        // Show a confirmation dialog before deleting
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Event");
        alert.setHeaderText("Are you sure you want to delete this event?");
        alert.setContentText("Event: " + selectedEvent.getEventName());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Delete the event from the database
            eventManager.deleteEvent(selectedEvent);
            // Refresh the events TableView
            eventsTableView.setItems(eventManager.getAllEvents());
        }
    }

    private void filterUsers() {
        // Get the selected role filters
        boolean showAdmin = adminCheckBox.isSelected();
        boolean showCoordinator = coordinatorCheckBox.isSelected();

        // Get the username filter (if any)
        String usernameFilter = usernameTextField.getText().trim().toLowerCase();

        // Get all users, filtered by role and username
        ObservableList<User> filteredUsers = FXCollections.observableArrayList();

        for (User user : userManager.getAllUsers()) {
            boolean matchesRole = false;
            if (showAdmin && user.getRankName().equals("Admin")) {
                matchesRole = true;
            }
            if (showCoordinator && user.getRankName().equals("Coordinator")) {
                matchesRole = true;
            }

            boolean matchesUsername = user.getUsername().toLowerCase().contains(usernameFilter);

            if ((showAdmin || showCoordinator) && matchesRole && matchesUsername) {
                filteredUsers.add(user);
            } else if (usernameFilter.isEmpty() && (showAdmin || showCoordinator) && matchesRole) {
                filteredUsers.add(user);
            } else if (usernameFilter.isEmpty() && !showAdmin && !showCoordinator) {
                filteredUsers.add(user); // Show all users if no filters are selected
            }
        }

        // Update the table view with the filtered users
        usersTableView.setItems(filteredUsers);
    }

    private void filterEvents() {
        boolean missingCoordinatorSelected = missingCoordinatorCheckBox.isSelected();
        String eventNameFilter = eventNameTextField.getText().toLowerCase();

        // Get all events
        ObservableList<Event> allEvents = eventManager.getAllEvents();

        // Filter the events based on the event name and the checkbox
        ObservableList<Event> filteredEvents = FXCollections.observableArrayList();

        for (Event event : allEvents) {
            boolean matchesName = event.getEventName().toLowerCase().contains(eventNameFilter);  // Filter by event name (case-insensitive)
            boolean matchesCoordinator = !missingCoordinatorSelected || event.getCoordinatorID() == 0;  // Filter by coordinator if checkbox is selected

            // Only add event if it matches both criteria
            if (matchesName && matchesCoordinator) {
                filteredEvents.add(event);
            }
        }

        // Update the TableView with the filtered events
        eventsTableView.setItems(filteredEvents);
    }

    private void signOut(ActionEvent event) {
        try {
            // Close the current Admin window
            closeAdminWindow(event);

            // Open the login window
            openLoginWindow();
        } catch (Exception e) {
            showErrorDialog("Error","Failed to close the current Admin window");
        }
    }

    private void closeAdminWindow(ActionEvent event) {
        // Get the current window and close it
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void openLoginWindow() throws Exception {
        // Load the login view and show it
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/dk/easv/ticketbar2/login-view.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Login");
        stage.show();
    }

    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
