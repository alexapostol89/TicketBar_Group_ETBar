package dk.easv.ticketbar2.gui.controllers;

import dk.easv.ticketbar2.be.Events;
import dk.easv.ticketbar2.bll.EventsManager;
import dk.easv.ticketbar2.bll.SessionManager;
import dk.easv.ticketbar2.bll.UsersManager;
import dk.easv.ticketbar2.dal.exceptions.EventsException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
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
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CoordinatorController {

    @FXML
    private Label userName;
    @FXML
    private Button addEvents;
    @FXML
    private FlowPane contentPane;

    private final EventsManager eventsManager = new EventsManager();
    private final UsersManager userManager = new UsersManager();
    private final SessionManager sessionManager = new SessionManager();

    private VBox lastSelectedVBox = null;

    @FXML
    public void initialize() throws EventsException {
        int userID = SessionManager.getCurrentUserID();
        String fullName = userManager.getFullNameByUserID(userID);
        userName.setText(fullName);

        loadExistingEvents();

        addEvents.setOnAction(event -> openAddEditEventWindow(null));
        if (!contentPane.getChildren().contains(addEvents)) {
            contentPane.getChildren().add(addEvents);
        }


        //contentPane.getChildren().add(addEvents); // Ensure add button is added initially
    }

    public void loadExistingEvents() throws EventsException {
        List<Events> events = eventsManager.getEvents();
        for (Events event : events) {
            updateCoordinatorView(event.getEventName(), event.getEventImagePath(), event.getEventID(), event.getStartDateTime());
        }
    }

    private void openAddEditEventWindow(Integer eventID) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dk/easv/ticketbar2/add-event.fxml"));
            Parent root = loader.load();

            AddEventsController editEventsController = loader.getController();
            editEventsController.setCoordinatorController(this); // Allow callback

            Stage stage = new Stage();
            stage.setTitle(eventID != null ? "Edit Event" : "Add Event");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException | EventsException e) {
            e.printStackTrace();
        }
    }

    public void updateCoordinatorView(String eventName, String imagePath, int eventID, String startDateTime) {
        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(10));
        vbox.setStyle(
                "-fx-background-color: #ffffff;" +
                        "-fx-background-radius: 15;" +
                        "-fx-border-radius: 15;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0.3, 0, 4);" +
                        "-fx-padding: 10;" +
                        "-fx-spacing: 10;" +
                        "-fx-alignment: top_center;"
        );

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

        String formattedDateTime = startDateTime;
        try {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM '-' HH:mm");
            LocalDateTime dateTime = LocalDateTime.parse(startDateTime, inputFormatter);
            formattedDateTime = dateTime.format(outputFormatter);
        } catch (Exception e) {
            System.out.println("Error formatting date: " + e.getMessage());
        }

        Label date = new Label(formattedDateTime);

        VBox.setMargin(imageView, new Insets(0, 0, 5, 0));
        VBox.setMargin(label, new Insets(5, 0, 0, 0));
        VBox.setMargin(date, new Insets(5, 0, 0, 0));

        vbox.getChildren().addAll(imageView, label, date);

        vbox.setOnMouseClicked(event -> {
            if (lastSelectedVBox != null) {
                String cleanedStyle = lastSelectedVBox.getStyle().replaceAll("-fx-border-color: #[0-9A-Fa-f]{6};?", "")
                        .replaceAll("-fx-border-width: \\d+;", "");
                lastSelectedVBox.setStyle(cleanedStyle);
            }

            vbox.setStyle(vbox.getStyle() + " -fx-border-color: #53D2DC; -fx-border-width: 2;");
            lastSelectedVBox = vbox;

            if (event.getClickCount() == 2) {
                int clickedEventID = (int) imageView.getUserData();
                openDetailsWindow(clickedEventID);
            }
        });

        contentPane.setMargin(vbox, new Insets(10, 10, 10, 10));

        // Insert event before the add button
        int addButtonIndex = contentPane.getChildren().indexOf(addEvents);
        if (addButtonIndex != -1) {
            contentPane.getChildren().add(addButtonIndex, vbox);
        } else {
            contentPane.getChildren().add(vbox); // fallback
        }
    }

    private void openDetailsWindow(int eventID) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dk/easv/ticketbar2/event-info.fxml"));
            Parent root = loader.load();

            EventDetailsController eventInfoController = loader.getController();
            eventInfoController.populateEventInfo(eventID);
            eventInfoController.setCoordinatorController(this);

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
            Node addButton = addEvents;
            contentPane.getChildren().clear();

            List<Events> events = eventsManager.getEvents();
            for (Events event : events) {
                updateCoordinatorView(event.getEventName(), event.getEventImagePath(), event.getEventID(), event.getStartDateTime());
            }

            contentPane.getChildren().add(addButton); // ensure Add button is always last
        } catch (EventsException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void logout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dk/easv/ticketbar2/login-view.fxml"));
            Parent root = loader.load();

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
