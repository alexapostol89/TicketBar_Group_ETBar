package dk.easv.ticketbar2.gui.controllers;

import dk.easv.ticketbar2.bll.EventsManager;
import dk.easv.ticketbar2.dal.exceptions.EventsException;
import dk.easv.ticketbar2.dal.web.EventsDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;


public class AddEventsController {

    @FXML
    private Button searchBtn;


    @FXML
    private TextField nameText, startDateText, endDateText, locationText, descriptionText, guidanceText, notesText, coordinatorText;

    @FXML
    private Button btnSave;  // Save button to save the entered name and image

    private String selectedImagePath;  // To hold the image file path

    private CoordinatorController coordinatorController;

    private final EventsManager eventsManager = new EventsManager();
    @FXML
    public void initialize() {
        // Open file chooser when the search button is clicked
        searchBtn.setOnAction(event -> openFileChooser());

        // Handle save button click
        btnSave.setOnAction(event -> {
            try {
                saveEvent();
            } catch (EventsException e) {
                throw new RuntimeException(e);
            }
        });

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
            try {
                // Define the target directory
                File mediaFolder = new File("media");
                if (!mediaFolder.exists()) {
                    mediaFolder.mkdirs(); // Create "media" folder if it doesn't exist
                }

                // Create destination file
                File destinationFile = new File(mediaFolder, selectedFile.getName());

                // Copy file to media folder
                java.nio.file.Files.copy(selectedFile.toPath(), destinationFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);

                // Store relative path
                selectedImagePath = "media/" + selectedFile.getName();
                System.out.println("Image saved to: " + selectedImagePath);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Method to save the event data
    private void saveEvent() throws EventsException {
        String eventName = nameText.getText();
        String startDate = startDateText.getText();
        String endDate = endDateText.getText();
        String location = locationText.getText();
        String description = descriptionText.getText();
        String locationGuide = guidanceText.getText();
        String notes = notesText.getText();
        int coordinatorId = Integer.parseInt(coordinatorText.getText());

        if (eventName.isEmpty()) {
            System.out.println("Event name is empty!");
            return;
        }

        // Save the event and get the eventID
        int eventID = eventsManager.saveEvent(eventName, selectedImagePath,startDate,endDate,location,description,locationGuide,notes,coordinatorId);
        System.out.println("New event ID: " + eventID); // Debugging

        // Add the new event to the coordinator controller
        if (coordinatorController != null) {
            coordinatorController.updateCoordinatorView(eventName, selectedImagePath, eventID); // Add the new event
        }

        // Close the window
        Stage stage = (Stage) btnSave.getScene().getWindow();
        stage.close();
    }

}