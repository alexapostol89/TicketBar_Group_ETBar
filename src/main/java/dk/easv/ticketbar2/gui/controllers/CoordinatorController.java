package dk.easv.ticketbar2.gui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

public class CoordinatorController {

    @FXML
    private Button addEvents;  // Button to open the "Add Event" window

    @FXML
    private FlowPane contentPane;  // FlowPane to dynamically add new images and labels

    @FXML
    public void initialize() {
        addEvents.setOnAction(event -> openEditEventWindow());
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

        Label label = new Label(eventName);
        label.setWrapText(true);
        label.setMaxWidth(200);

        VBox.setMargin(imageView, new javafx.geometry.Insets(0, 0, 5, 0));
        VBox.setMargin(label, new javafx.geometry.Insets(5, 0, 0, 0));

        vbox.getChildren().addAll(imageView, label);

        // ADD DOUBLE-CLICK EVENT TO OPEN 1234test.fxml
        imageView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                openDetailsWindow();
            }
        });

        contentPane.getChildren().add(vbox);
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
}
