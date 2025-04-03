package dk.easv.ticketbar2.gui.controllers;

import dk.easv.ticketbar2.be.Events;
import dk.easv.ticketbar2.be.Users;
import dk.easv.ticketbar2.bll.EventsManager;
import dk.easv.ticketbar2.bll.UsersManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Optional;

public class AdminViewController {

    //DAVID METHOD'S
    @FXML
    private TableView<Users> usersTableView;

    @FXML
    private TableColumn<Users, String> firstNameColumn;

    @FXML
    private TableColumn<Users, String> usernameColumn;

    @FXML
    private TableColumn<Users, String> rankColumn;

    @FXML
    private Label firstNameLabel, lastNameLabel, rankLabel, usernameLabel, emailLabel, phoneLabel, dateCreatedLabel, lastLoginLabel;

    @FXML
    private TableView<Events> eventsTableView;

    @FXML
    private TableColumn<Events, String> nameColumn;

    @FXML
    private TableColumn<Events, String> dateColumn;

    @FXML
    private TableColumn<Events, String> coordinatorColumn;

    @FXML
    private Label nameLabel, startDateLabel, endDateLabel, locationLabel, descriptionLabel, coordinatorLabel, guideLabel, notesLabel;

    @FXML
    private Button signOutButton1, signOutButton2, addUserButton, editUserButton, deleteUserButton, assignCoordinatorButton, deleteEventButton;

    @FXML
    private CheckBox adminCheckBox, coordinatorCheckBox, missingCoordinatorCheckBox;

    @FXML
    private TextField usernameTextField, eventNameTextField;

    private UsersManager usersManager = new UsersManager();
    private EventsManager eventsManager = new EventsManager();

    public void initialize() {
        // Setup Users TableView
        firstNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstname()));
        usernameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));
        rankColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRankName()));
        usersTableView.setItems(usersManager.getAllUsers());

        // Setup Events TableView
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEventName()));
        dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStartDateTime()));
        // coordinatorColumn.setCellValueFactory(cellData -> new SimpleStringProperty(Integer.toString(cellData.getValue().getCoordinatorID())));

        // Modify the cell value factory for the Coordinator column
        coordinatorColumn.setCellValueFactory(cellData -> {
            Events events = cellData.getValue();
            // Check if the event has a coordinator (CoordinatorID != 0)
            if (events.getCoordinatorID() == 0) {
                return new SimpleStringProperty("Not Assigned");
            } else {
                // Get the coordinator's username by CoordinatorID
                String coordinatorUsername = eventsManager.getCoordinatorUsername(events.getCoordinatorID());
                return new SimpleStringProperty(coordinatorUsername);
            }
        });

        eventsTableView.setItems(eventsManager.getAllEvents());

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

    private void populateUserDetails(Users users) {
        firstNameLabel.setText(users.getFirstname());
        lastNameLabel.setText(users.getLastname());
        rankLabel.setText(users.getRankName());
        usernameLabel.setText(users.getUsername());
        emailLabel.setText(users.getEmail());
        phoneLabel.setText(users.getPhone());
        dateCreatedLabel.setText(users.getCreatedDate());
        // Format and display last login date
        if (users.getLastLogin() != null) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDate = dateFormat.format(users.getLastLogin());
            lastLoginLabel.setText(formattedDate);
        } else {
            lastLoginLabel.setText("No login record");
        }
    }

    private void populateEventDetails(Events events) {
        nameLabel.setText(events.getEventName());
        startDateLabel.setText(events.getStartDateTime());
        endDateLabel.setText(events.getEndDateTime());
        locationLabel.setText(events.getLocation());
        descriptionLabel.setText(events.getDescription());

        // Fetch the coordinator's username and display it
        String coordinatorUsername = events.getCoordinatorID() == 0
                ? "Not Assigned"
                : eventsManager.getCoordinatorUsername(events.getCoordinatorID());

        coordinatorLabel.setText(coordinatorUsername);
        guideLabel.setText(events.getLocationGuide());
        notesLabel.setText(events.getNotes());
    }

    private void handleAssignCoordinator(ActionEvent event) {
        // Get the selected event from the TableView
        Events selectedEvent = eventsTableView.getSelectionModel().getSelectedItem();

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

    private void openAssignCoordinatorDialog(Events selectedEvent) {
        // Create a new stage for the dialog
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);  // Make sure it's modal
        dialogStage.setTitle("Assign Coordinator");

        // Create a ListView for the coordinators
        ListView<String> coordinatorListView = new ListView<>();

        // Fetch all users with the "Coordinator" role
        ObservableList<String> coordinatorUsernames = FXCollections.observableArrayList();
        for (Users users : usersManager.getAllUsers()) {
            if (users.getRankName().equals("Coordinator")) {
                coordinatorUsernames.add(users.getUsername());
            }
        }

        coordinatorListView.setItems(coordinatorUsernames);

        // Confirm button action
        Button confirmButton = new Button("Confirm");
        confirmButton.setOnAction(e -> {
            String selectedCoordinatorUsername = coordinatorListView.getSelectionModel().getSelectedItem();

            if (selectedCoordinatorUsername != null) {
                // Find user by Username (i.e. fetch the UserID from the selected coordinator's username)
                Users selectedCoordinator = null;
                for (Users users : usersManager.getAllUsers()) {
                    if (users.getUsername().equals(selectedCoordinatorUsername)) {
                        selectedCoordinator = users;
                        break;
                    }
                }

                if (selectedCoordinator != null) {
                    // Update the selected event coordinator
                    selectedEvent.setCoordinatorID(selectedCoordinator.getUserid());

                    // Update the event
                    eventsManager.updateEventCoordinator(selectedEvent);

                    // Close the dialog window and refresh the table view
                    dialogStage.close();
                    eventsTableView.refresh();
                } else {
                    showErrorDialog("Error", "Coordinator not found.");
                }
            } else {
                showErrorDialog("Error", "Please select a coordinator.");
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
            usersTableView.setItems(FXCollections.observableArrayList(usersManager.getAllUsers()));

        } catch (Exception e) {
            showErrorDialog("Error", "Failed to open the Add User dialog");
        }
    }

    // Method to handle Edit User button
    private void handleEditUser(ActionEvent event) {
        Users selectedUser = usersTableView.getSelectionModel().getSelectedItem();

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
                usersTableView.setItems(FXCollections.observableArrayList(usersManager.getAllUsers()));

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
        Users selectedUser = usersTableView.getSelectionModel().getSelectedItem();

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
                usersManager.deleteUser(selectedUser);
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
        Events selectedEvent = eventsTableView.getSelectionModel().getSelectedItem();

        if (selectedEvent == null) {
            // Show an error message if no event is selected
            showErrorDialog("Error", "Please select an event to delete");
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
            eventsManager.deleteEvent(selectedEvent);
            // Refresh the events TableView
            eventsTableView.setItems(eventsManager.getAllEvents());
        }
    }

    private void filterUsers() {
        // Get the selected role filters
        boolean showAdmin = adminCheckBox.isSelected();
        boolean showCoordinator = coordinatorCheckBox.isSelected();

        // Get the username filter (if any)
        String usernameFilter = usernameTextField.getText().trim().toLowerCase();

        // Get all users, filtered by role and username
        ObservableList<Users> filteredUsers = FXCollections.observableArrayList();

        for (Users users : usersManager.getAllUsers()) {
            boolean matchesRole = false;
            if (showAdmin && users.getRankName().equals("Admin")) {
                matchesRole = true;
            }
            if (showCoordinator && users.getRankName().equals("Coordinator")) {
                matchesRole = true;
            }

            boolean matchesUsername = users.getUsername().toLowerCase().contains(usernameFilter);

            if ((showAdmin || showCoordinator) && matchesRole && matchesUsername) {
                filteredUsers.add(users);
            } else if (usernameFilter.isEmpty() && (showAdmin || showCoordinator) && matchesRole) {
                filteredUsers.add(users);
            } else if (usernameFilter.isEmpty() && !showAdmin && !showCoordinator) {
                filteredUsers.add(users); // Show all users if no filters are selected
            }
        }

        // Update the table view with the filtered users
        usersTableView.setItems(filteredUsers);
    }

    private void filterEvents() {
        boolean missingCoordinatorSelected = missingCoordinatorCheckBox.isSelected();
        String eventNameFilter = eventNameTextField.getText().toLowerCase();

        // Get all events
        ObservableList<Events> allEvents = eventsManager.getAllEvents();

        // Filter the events based on the event name and the checkbox
        ObservableList<Events> filteredEvents = FXCollections.observableArrayList();

        for (Events events : allEvents) {
            boolean matchesName = events.getEventName().toLowerCase().contains(eventNameFilter);  // Filter by event name (case-insensitive)
            boolean matchesCoordinator = !missingCoordinatorSelected || events.getCoordinatorID() == 0;  // Filter by coordinator if checkbox is selected

            // Only add event if it matches both criteria
            if (matchesName && matchesCoordinator) {
                filteredEvents.add(events);
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
            showErrorDialog("Error", "Failed to close the current Admin window");
        }
    }

    private void closeAdminWindow(ActionEvent event) {
        // Get the current window and close it
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void openLoginWindow() throws Exception {
        // Load the login view and show it
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/dk/easv/ticketbar2/oldlogin-view.fxml"));
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

