package dk.easv.ticketbar2.gui.controllers;

import dk.easv.ticketbar2.be.Events;
import dk.easv.ticketbar2.be.Users;
import dk.easv.ticketbar2.bll.EventsManager;
import dk.easv.ticketbar2.bll.SessionManager;
import dk.easv.ticketbar2.bll.UsersManager;
import dk.easv.ticketbar2.dal.exceptions.EventsException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class CoordinatorController {

    @FXML
    private Label userName;
    @FXML
    private Button addEvents, editEvents;
    @FXML
    private FlowPane contentPane;  // FlowPane to dynamically add new images and labels

    private final EventsManager eventsManager = new EventsManager();
    private final UsersManager userManager = new UsersManager();
    private final SessionManager sessionManager = new SessionManager();

    private VBox lastSelectedVBox = null; // Track the last selected event


    @FXML
    public void initialize() throws EventsException {

        int userID = SessionManager.getCurrentUserID();
        String fullName = userManager.getFullNameByUserID(userID);
        userName.setText(fullName);

        loadExistingEvents();
        addEvents.setOnAction(event -> openAddEditEventWindow(null)); // Pass null for add mode
    }

    // Load existing events into the content pane
    public void loadExistingEvents() throws EventsException {
        List<Events> events = eventsManager.getEvents();
        for (Events event : events) {
            updateCoordinatorView(event.getEventName(), event.getEventImagePath(), event.getEventID(), event.getStartDateTime());
        }
    }

    // Open the "Add Event" window
    private void openAddEditEventWindow(Integer eventID) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dk/easv/ticketbar2/add-event.fxml"));
            Parent root = loader.load();

            AddEventsController editEventsController = loader.getController();
            editEventsController.setCoordinatorController(this);


            Stage stage = new Stage();
            stage.setTitle(eventID != null ? "Edit Event" : "Add Event");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException | EventsException e) {
        }
    }


    // Update the coordinator view with the list of events
    public void updateCoordinatorView(String eventName, String imagePath, int eventID,String StartDateTime) {
        VBox vbox = new VBox();
        vbox.setSpacing(10);


        vbox.setPadding(new javafx.geometry.Insets(10));
        vbox.setStyle("-fx-border-color: transparent; -fx-border-width: 2;"); // Default no border
        vbox.setStyle("-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 10, 0.5, 0, 0)");


        ImageView imageView = new ImageView();
        if (imagePath != null && !imagePath.isEmpty()) {
            File file = new File(imagePath);
            if (file.exists()) {
                Image image = new Image(file.toURI().toString());
                imageView.setImage(image);
                imageView.setFitHeight(150);
                imageView.setFitWidth(200);
            }
        }

        imageView.setUserData(eventID);

        Label label = new Label(eventName);

        // Format StartDateTime string
        String formattedDateTime = StartDateTime;
        try {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy ', ' HH:mm 'h'");
            LocalDateTime dateTime = LocalDateTime.parse(StartDateTime, inputFormatter);
            formattedDateTime = dateTime.format(outputFormatter);
        } catch (Exception e) {
            System.out.println("Error formatting date: " + e.getMessage());
        }

        Label date = new Label(formattedDateTime);


        label.setWrapText(true);
        label.setMaxWidth(200);

        VBox.setMargin(imageView, new javafx.geometry.Insets(0, 0, 5, 0));
        VBox.setMargin(label, new javafx.geometry.Insets(5, 0, 0, 0));
        VBox.setMargin(date, new javafx.geometry.Insets(5, 0, 0, 0));


        vbox.getChildren().addAll(imageView, label, date);

        vbox.setOnMouseClicked(event -> {
            // Remove the border from the previously selected event
            if (lastSelectedVBox != null) {
                lastSelectedVBox.setStyle("-fx-border-color: transparent; -fx-border-width: 2;");
            }

            // Apply a blue border to the selected event
            vbox.setStyle("-fx-border-color: #9AD0E7; -fx-border-width: 2; -fx-border-radius: 5;");
            lastSelectedVBox = vbox;

            // Enable edit and delete buttons when an event is selected


           if (event.getClickCount() == 2) { // Open details window on double click
                int clickedEventID = (int) imageView.getUserData();
                openDetailsWindow(clickedEventID);
            }
        });

        contentPane.getChildren().add(vbox);
    }

    // Open the details window for the selected event
    private void openDetailsWindow(int eventID) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dk/easv/ticketbar2/event-info.fxml"));
            Parent root = loader.load();

            EventDetailsController eventInfoController = loader.getController();
            eventInfoController.populateEventInfo(eventID);
            eventInfoController.setCoordinatorController(this); // Pass reference!

            Stage stage = new Stage();
            stage.setTitle("Event Details");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException | EventsException e) {
            e.printStackTrace();
        }
    }


    public void refreshEvents() {
        try {
            // Clear existing events
            contentPane.getChildren().clear();

            // Reload events from database
            List<Events> events = eventsManager.getEvents();

            // Rebuild the UI
            for (Events event : events) {
                updateCoordinatorView(event.getEventName(), event.getEventImagePath(), event.getEventID(), event.getStartDateTime());
            }
        } catch (EventsException e) {
            e.printStackTrace();
            // Show error message if needed
        }
    }

    // Method for logging out
    @FXML
    private void logout(ActionEvent event) {
        try {
            // Load Login screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dk/easv/ticketbar2/login-view.fxml"));
            Parent root = loader.load();

            // Close the current stage
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            Stage stage = new Stage();
            stage.setTitle("Login");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



