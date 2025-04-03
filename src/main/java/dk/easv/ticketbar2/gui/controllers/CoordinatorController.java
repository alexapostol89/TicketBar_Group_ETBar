package dk.easv.ticketbar2.gui.controllers;

import dk.easv.ticketbar2.be.Events;
import dk.easv.ticketbar2.bll.EventsManager;
import dk.easv.ticketbar2.dal.exceptions.EventsException;
import javafx.collections.ObservableList;
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
import java.util.List;
import java.util.Optional;

public class CoordinatorController {
    @FXML
    private Button assignCoordinatorButton;
    @FXML
    private Label nameLabel, startDateLabel, descriptionLabel;
    @FXML
    private Button addEvents, editEvents;
    @FXML
    private Button deleteBtn;

    @FXML
    private FlowPane contentPane;  // FlowPane to dynamically add new images and labels

    private final EventsManager eventsManager = new EventsManager();

    private VBox lastSelectedVBox = null; // Track the last selected event

    private EventDetailsController eventDetailsController;

    @FXML
    public void initialize() throws EventsException {
        // Initially disable the delete and edit buttons
        deleteBtn.setDisable(true);
        editEvents.setDisable(true);
        loadExistingEvents();
        addEvents.setOnAction(event -> openAddEditEventWindow(null)); // Pass null for add mode

        deleteBtn.setOnAction(this::onActionDelete);

        editEvents.setOnAction(event -> {
            if (lastSelectedVBox != null) {
                ImageView imageView = (ImageView) lastSelectedVBox.getChildren().get(0);
                int eventID = (int) imageView.getUserData();
                openAddEditEventWindow(eventID);
            }
        });
    }

    // Load existing events into the content pane
    public void loadExistingEvents() throws EventsException {
        List<Events> events = eventsManager.getEvents();
        for (Events event : events) {
            updateCoordinatorView(event.getEventName(), event.getEventImagePath(), event.getEventID());

        }
    }

    // Open the "Add Event" window
    private void openAddEditEventWindow(Integer eventID) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dk/easv/ticketbar2/add-event.fxml"));
            Parent root = loader.load();

            AddEventsController editEventsController = loader.getController();
            editEventsController.setCoordinatorController(this);

            if (eventID != null) {
                // Edit mode - load existing event data
                Events eventToEdit = eventsManager.getEventById(eventID);
                editEventsController.setEventToEdit(eventToEdit);
            }

            Stage stage = new Stage();
            stage.setTitle(eventID != null ? "Edit Event" : "Add Event");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException | EventsException e) {
        }
    }


    // Update the coordinator view with the list of events
    public void updateCoordinatorView(String eventName, String imagePath, int eventID) {
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


        label.setWrapText(true);
        label.setMaxWidth(200);

        VBox.setMargin(imageView, new javafx.geometry.Insets(0, 0, 5, 0));
        VBox.setMargin(label, new javafx.geometry.Insets(5, 0, 0, 0));

        vbox.getChildren().addAll(imageView, label);

        vbox.setOnMouseClicked(event -> {
            // Remove the border from the previously selected event
            if (lastSelectedVBox != null) {
                lastSelectedVBox.setStyle("-fx-border-color: transparent; -fx-border-width: 2;");
            }

            // Apply a blue border to the selected event
            vbox.setStyle("-fx-border-color: #9AD0E7; -fx-border-width: 2; -fx-border-radius: 5;");
            lastSelectedVBox = vbox;

            // Enable edit and delete buttons when an event is selected
            editEvents.setDisable(false);
            deleteBtn.setDisable(false);

            if (event.getClickCount() == 1) { // Show event details on single click
                int clickedEventID = (int) imageView.getUserData();
                try {
                    Events clickedEvent = eventsManager.getEventById(clickedEventID);
                    if (clickedEvent != null) {
                        nameLabel.setText(clickedEvent.getEventName());
                        startDateLabel.setText(clickedEvent.getStartDateTime());
                        descriptionLabel.setText(clickedEvent.getDescription());

                    }
                } catch (EventsException e) {
                }
            } else if (event.getClickCount() == 2) { // Open details window on double click
                int clickedEventID = (int) imageView.getUserData();
                openDetailsWindow(clickedEventID);
            }
        });

        contentPane.getChildren().add(vbox);
    }

    // Open the details window for the selected event
    private void openDetailsWindow(int eventID) {
        System.out.println("Opening details for event ID: " + eventID); // Debugging
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dk/easv/ticketbar2/event-info.fxml"));
            Parent root = loader.load();

            // Get the EventInfo controller and pass the eventID
            EventDetailsController eventInfoController = loader.getController();
            eventInfoController.populateEventInfo(eventID);

            Stage stage = new Stage();
            stage.setTitle("Event Details");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException | EventsException e) {
            e.printStackTrace();
        }
    }


    // Method to delete the selected event from the app and database
    @FXML
    private void onActionDelete(ActionEvent event) {
        if (lastSelectedVBox != null) {
            // Show confirmation alert
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Are you sure you want to delete this event?");
            alert.setContentText("Event: " + nameLabel.getText() + "\n\nThis action cannot be undone.");

            // Wait for user response
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Get the event ID from the selected event
                ImageView imageView = (ImageView) lastSelectedVBox.getChildren().get(0); // First child is the image
                int eventID = (int) imageView.getUserData();

                // Delete the event from the database
                try {
                    boolean success = eventsManager.deleteEvent(eventID);
                    if (success) {
                        // Remove the event from the UI
                        contentPane.getChildren().remove(lastSelectedVBox);
                        System.out.println("Event deleted successfully from the database and UI.");
                    } else {
                        System.out.println("Failed to delete the event from the database.");
                    }
                } catch (EventsException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Event deletion canceled.");
            }
        } else {
            // Show a warning alert if no event is selected
            Alert warningAlert = new Alert(Alert.AlertType.WARNING);
            warningAlert.setTitle("No Event Selected");
            warningAlert.setHeaderText(null);
            warningAlert.setContentText("Please select an event to delete.");
            warningAlert.showAndWait();
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
                updateCoordinatorView(event.getEventName(), event.getEventImagePath(), event.getEventID());
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



