package dk.easv.ticketbar2.gui.controllers;

import dk.easv.ticketbar2.be.Users;
import dk.easv.ticketbar2.bll.UsersManager;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class AddEditUsersController {

    @FXML
    private TextField usernameField, firstNameField, lastNameField, emailField, phoneField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ComboBox<String> rankComboBox;

    @FXML
    private Button saveButton;

    private UsersManager usersManager = new UsersManager();
    private Users user; // To hold the user when editing
    private String dialogType; // To distinguish between add and edit

    public void initialize() {
        // Populate the ComboBox with available ranks
        rankComboBox.getItems().addAll("Admin", "Coordinator");
    }

    // Set dialog type (add or edit)
    public void setDialogType(String dialogType) {
        this.dialogType = dialogType;
    }

    // Set user data for editing
    public void setUser(Users user) {
        this.user = user;

        // Populate fields with existing user data
        if (user != null) {
            usernameField.setText(user.getUsername());
            firstNameField.setText(user.getFirstName());
            lastNameField.setText(user.getLastName());
            emailField.setText(user.getEmail());
            phoneField.setText(user.getPhone());
            rankComboBox.setValue(user.getRankName());
        }
    }

    // Handle Save button
    @FXML
    public void handleSave() {
        // Debugging statement
        System.out.println("handleSave called");

        // Get the password from the input field
        String password = passwordField.getText();

        Task<Boolean> saveTask = new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                boolean success = false;

                if (dialogType.equals("add")) {
                    // Debugging statement
                    System.out.println("Adding user");

                    // Create a new user and add it to the database
                    Users newUser = new Users();
                    newUser.setUsername(usernameField.getText());
                    newUser.setFirstName(firstNameField.getText());
                    newUser.setLastName(lastNameField.getText());
                    newUser.setEmail(emailField.getText());
                    newUser.setPhone(phoneField.getText());
                    newUser.setRank(rankComboBox.getValue().equals("Admin") ? 1 : 2);

                    // Passing the password
                    success = usersManager.addUser(newUser, password);

                    // Debugging statement
                    System.out.println("addUser result: " + success);

                } else if (dialogType.equals("edit")) {
                    // Debugging statement
                    System.out.println("Editing user");

                    // Update the existing user
                    user.setUsername(usernameField.getText());
                    user.setFirstName(firstNameField.getText());
                    user.setLastName(lastNameField.getText());
                    user.setEmail(emailField.getText());
                    user.setPhone(phoneField.getText());
                    user.setRank(rankComboBox.getValue().equals("Admin") ? 1 : 2);

                    // Use the new password if edited
                    String existingPassword = passwordField.getText();
                    // Passing the new or existing password
                    success = usersManager.editUser(user, existingPassword);

                    // Debugging statement
                    System.out.println("editUser result: " + success);
                }
                return success;
            }
        };

        // Handling the task completion
        saveTask.setOnSucceeded(event -> {
            boolean success = saveTask.getValue();
            if (success) {
                showInfoDialog("User Created", "New user created successfully.");
            } else {
                showErrorDialog("Error", "Failed to create user.");
            }
            // Close the dialog
            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.close();
        });

        saveTask.setOnFailed(event -> {
            showErrorDialog("Error", "An error occurred while saving the user.");
            saveTask.getException().printStackTrace();
            // Close the dialog
            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.close();
        });

        // Start the task in the background
        // new Thread(saveTask).start();

        saveTask.run(); // This will execute the task on the JavaFX Application Thread
    }

    // Show an error dialog
    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Show an info dialog
    private void showInfoDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
