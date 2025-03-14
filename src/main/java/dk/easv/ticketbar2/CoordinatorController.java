package dk.easv.ticketbar2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import javafx.scene.image.ImageView;
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
        // Open EditEvents window when "Add Event" button is clicked
        addEvents.setOnAction(event -> openEditEventWindow());
    }

    // Method to open EditEvents window
    private void openEditEventWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dk/easv/ticketbar2/editEvents.fxml"));
            Parent root = loader.load();

            // Get the controller for EditEvents
            EditEvents editEventsController = loader.getController();

            // Set this CoordinatorController in EditEvents to pass data back
            editEventsController.setCoordinatorController(this);

            Stage stage = new Stage();
            stage.setTitle("Add Event");
            stage.initModality(Modality.APPLICATION_MODAL);  // Block interaction with the main window
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to update the event name and image in CoordinatorController
    public void updateCoordinatorView(String eventName, String imagePath) {
        // Create a new VBox for each image and label
        VBox vbox = new VBox();
        vbox.setSpacing(10); // Space between image and label
        vbox.setPadding(new javafx.geometry.Insets(10)); // Padding around the VBox

        // Add Image
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

        // Add Label for event name
        Label label = new Label(eventName);
        label.setWrapText(true);  // Allow the label text to wrap if it's too long
        label.setMaxWidth(200);  // Set a max width to ensure the label doesn't stretch too much

        // Optional: Set margin for individual components within VBox
        VBox.setMargin(imageView, new javafx.geometry.Insets(0, 0, 5, 0)); // Margin only below the image
        VBox.setMargin(label, new javafx.geometry.Insets(5, 0, 0, 0));  // Margin only above the label

        // Add image and label to VBox
        vbox.getChildren().addAll(imageView, label);

        // Add the VBox to the FlowPane
        contentPane.getChildren().add(vbox);
    }
}
