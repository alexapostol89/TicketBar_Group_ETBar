package dk.easv.ticketbar2.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class EditEvents {

    @FXML
    private Button searchBtn;

    @FXML
    private TextField nameId;  // TextField to enter the name

    @FXML
    private Button btnSave;  // Save button to save the entered name and image

    private String selectedImagePath;  // To hold the image file path

    private CoordinatorController coordinatorController;

    // Method to initialize the window (optional)
    @FXML
    public void initialize() {
        // Open file chooser when choice box is triggered
        searchBtn.setOnAction(event -> openFileChooser());

        // Handle save button click
        btnSave.setOnAction(event -> saveEvent());
    }

    // Set reference to CoordinatorController so that we can pass data back
    public void setCoordinatorController(CoordinatorController coordinatorController) {
        this.coordinatorController = coordinatorController;
    }

    // Method to open a file chooser and select an image
    private void openFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            selectedImagePath = selectedFile.getAbsolutePath();
        }
    }

    // Method to save the event data
    private void saveEvent() {
        String eventName = nameId.getText();
        if (eventName.isEmpty()) {
            System.out.println("Event name is empty!");
            return;
        }

        // Update the CoordinatorController with the entered name and selected image
        if (coordinatorController != null) {
            coordinatorController.updateCoordinatorView(eventName, selectedImagePath);
        }

        // Close the EditEvents window
        Stage stage = (Stage) btnSave.getScene().getWindow();
        stage.close();
    }
}
