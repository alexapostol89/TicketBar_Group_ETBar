package dk.easv.ticketbar2.gui.controllers;

import dk.easv.ticketbar2.be.Events;
import dk.easv.ticketbar2.be.Users;
import dk.easv.ticketbar2.bll.EventsManager;
import dk.easv.ticketbar2.bll.UsersManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class AdminViewController {

    @FXML
    private TableView<Users> usersTableView;

    @FXML
    private TableColumn<Users, String> firstNameColumn;

    @FXML
    private TableColumn<Users, String> usernameColumn;

    @FXML
    private TableColumn<Users, String> rankColumn;

    @FXML
    private Label firstNameLabel, lastNameLabel, rankLabel, usernameLabel, passwordLabel, emailLabel, phoneLabel, dateCreatedLabel, lastLoginLabel;

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
    private Button signOutButton1, signOutButton2;

    private UsersManager usersManager = new UsersManager();
    private EventsManager eventsManager = new EventsManager();

    public void initialize() {
        // Setup Users TableView
        firstNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName()));
        usernameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));
        rankColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRankName()));
        usersTableView.setItems(usersManager.getAllUsers());

        // Setup Events TableView
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEventName()));
        dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStartDateTime()));
        coordinatorColumn.setCellValueFactory(cellData -> new SimpleStringProperty(Integer.toString(cellData.getValue().getCoordinatorID())));
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

        // Sign out button action
        signOutButton1.setOnAction(this::signOut);
        signOutButton2.setOnAction(this::signOut);
    }

    private void populateUserDetails(Users user) {
        firstNameLabel.setText(user.getFirstName());
        lastNameLabel.setText(user.getLastName());
        rankLabel.setText(user.getRankName());
        usernameLabel.setText(user.getUsername());
        emailLabel.setText(user.getEmail());
        phoneLabel.setText(user.getPhone());
        dateCreatedLabel.setText(user.getCreatedDate());
        lastLoginLabel.setText(user.getLastLogin());
    }

    private void populateEventDetails(Events event) {
        nameLabel.setText(event.getEventName());
        startDateLabel.setText(event.getStartDateTime());
        endDateLabel.setText(event.getEndDateTime());
        locationLabel.setText(event.getLocation());
        descriptionLabel.setText(event.getDescription());
        coordinatorLabel.setText(Integer.toString(event.getCoordinatorID()));
        guideLabel.setText(event.getLocationGuide());
        notesLabel.setText(event.getNotes());
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

    // Method to close Admin window
    private void closeAdminWindow(ActionEvent event) {
        // Get the current window and close it
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    // Method to open the login window
    private void openLoginWindow() throws Exception {
        // Load the login view and show it
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/dk/easv/ticketbar2/login-view.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Login");
        stage.show();
    }

    /**
     *
     * A helper method for error handling
     * Displays a user-friendly dialog when an error occurs (FXML loading error, invalid credentials, etc.)
     * You no longer need to import IOException
     */
    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
