package dk.easv.ticketbar2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class CoordinatorController {

    @FXML
    private ImageView imageBar;

    @FXML
    private Button editEvents;

    private boolean imageClicked = false; // Track if image was clicked

    @FXML
    public void initialize() {
        // Load the image into imageBar
        Image image = new Image(getClass().getResourceAsStream("/dk/easv/ticketbar2/pictures/imageBar.jpg"));
        imageBar.setImage(image);

        // Add click event to imageBar
        imageBar.setOnMouseClicked(event -> {
            imageClicked = true; // Mark that the image was clicked
        });

        // Add action event to editEvents button
        editEvents.setOnAction(event -> {
            if (imageClicked) {
                openNewWindow("/dk/easv/ticketbar2/editEvents.fxml", "Edit Event");
                imageClicked = false; // Reset after opening the window
            }
        });
    }

    private void openNewWindow(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.initModality(Modality.APPLICATION_MODAL); // Block interaction with the main window
            stage.setScene(new Scene(root));

            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Handle FXML loading errors
        }
    }
}

