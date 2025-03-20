package dk.easv.ticketbar2.gui.controllers;

import dk.easv.ticketbar2.be.Events;
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
import java.util.List;

public class CoordinatorController {

    @FXML
    private Button addEvents;  // Button to open the "Add Event" window

    @FXML
    private FlowPane contentPane;  // FlowPane to dynamically add new images and labels

    @FXML
    private VBox eventBox;  // Event template VBox (defined in SceneBuilder)

    //@FXML
    //private ImageView eventImage;  // ImageView inside the template VBox

    //@FXML
    //private Label eventLabel;  // Label inside the template VBox

    @FXML
    public void initialize() {
        contentPane.setHgap(10);  // Horizontal gap between event boxes
        contentPane.setVgap(10);
        addEvents.setOnAction(event -> openEditEventWindow());
        loadExistingEvents();
    }

    private void loadExistingEvents() {
        EventsDAO eventsDAO = new EventsDAO();
        List<Events> events = eventsDAO.getEvents();  // Fetch events from REST API

        for (Events event : events) {
            updateCoordinatorView(event.getEvent_name(), event.getEvent_image_path());
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

    public void updateCoordinatorView(String eventName, String imagePath) {
        // Create a new VBox dynamically (instead of using eventBox)
        VBox newEventBox = new VBox();
        newEventBox.setSpacing(10);
        newEventBox.setAlignment(javafx.geometry.Pos.CENTER);

        // Create ImageView dynamically
        ImageView newImageView = new ImageView();
        newImageView.setFitWidth(200);
        newImageView.setFitHeight(150);

        if (imagePath != null && !imagePath.isEmpty()) {
            File file = new File(imagePath);
            if (file.exists()) {
                Image image = new Image(file.toURI().toString());
                newImageView.setImage(image);
            }
        }

        // Create Label dynamically
        Label newLabel = new Label(eventName);
        newLabel.setStyle("-fx-background-color: rgba(255, 255, 255, 0.15);" + "-fx-background-radius: 20;\n" + "-fx-border-radius: 20;");
        newLabel.setWrapText(true);
        newLabel.setMaxWidth(200);

        newEventBox.getChildren().addAll(newImageView, newLabel);

        // Add event double-click to open event details
        newImageView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                openDetailsWindow();
            }
        });

        contentPane.getChildren().add(newEventBox);
    }


    private void openDetailsWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dk/easv/ticketbar2/EventInfo.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Event Details");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void logout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dk/easv/ticketbar2/login-view.fxml"));
            Parent root = loader.load();

            // Close current stage
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