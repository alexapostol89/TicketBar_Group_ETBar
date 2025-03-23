package dk.easv.ticketbar2.gui.controllers;

import dk.easv.ticketbar2.be.Events;
import dk.easv.ticketbar2.dal.exceptions.EventsException;
import dk.easv.ticketbar2.dal.web.EventsDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CoordinatorController {

    @FXML
    private Button addEvents;  // Button to open the "Add Event" window

    @FXML
    private FlowPane contentPane;  // FlowPane to dynamically add new images and labels

    @FXML
    public void initialize() throws EventsException {
        addEvents.setOnAction(event -> openEditEventWindow());
        loadExistingEvents();
    }

    public void loadExistingEvents() throws EventsException {
        EventsDAO eventsDAO = new EventsDAO();
        List<Events> events = eventsDAO.getAllEvents();

        for (Events event : events) {
            updateCoordinatorView(event.getEventName(), event.getEventImagePath(), event.getEventID());
        }
    }

    private void openEditEventWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dk/easv/ticketbar2/add-edit-event.fxml"));
            Parent root = loader.load();

            EditEvents editEventsController = loader.getController();
            editEventsController.setCoordinatorController(this);

            Stage stage = new Stage();
            stage.setTitle("Add Event");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateCoordinatorView(String eventName, String imagePath, int eventID) {
        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setPadding(new javafx.geometry.Insets(10));

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

        // Store the eventID in the ImageView's userData property
        imageView.setUserData(eventID);

        Label label = new Label(eventName);
        label.setWrapText(true);
        label.setMaxWidth(200);

        VBox.setMargin(imageView, new javafx.geometry.Insets(0, 0, 5, 0));
        VBox.setMargin(label, new javafx.geometry.Insets(5, 0, 0, 0));

        vbox.getChildren().addAll(imageView, label);

        // Add double-click event to open EventInfo window
        imageView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                int clickedEventID = (int) imageView.getUserData(); // Retrieve the eventID from the ImageView
                System.out.println("Clicked event ID: " + clickedEventID); // Debugging
                openDetailsWindow(clickedEventID); // Open the details window with the eventID
            }
        });

        contentPane.getChildren().add(vbox);
    }



    private void openDetailsWindow(int eventID) {
        System.out.println("Opening details for event ID: " + eventID); // Debugging
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dk/easv/ticketbar2/EventInfo.fxml"));
            Parent root = loader.load();

            // Get the EventInfo controller and pass the eventID
            EventInfo eventInfoController = loader.getController();
            eventInfoController.populateEventInfo(eventID);

            Stage stage = new Stage();
            stage.setTitle("Event Details");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException | EventsException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void logout(ActionEvent event) {
        try{
            //Load Login screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dk/easv/ticketbar2/login-view.fxml"));
            Parent root = loader.load();

            //Closes actual stage
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