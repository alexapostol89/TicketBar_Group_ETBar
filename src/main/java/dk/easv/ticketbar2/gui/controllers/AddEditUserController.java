package dk.easv.ticketbar2.gui.controllers;

import dk.easv.ticketbar2.be.Users;
import dk.easv.ticketbar2.bll.UsersManager;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.regex.Pattern;

public class AddEditUserController {
    @FXML
    private TextField usernameField, firstNameField, lastNameField, emailField, phoneField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ComboBox<String> rankComboBox;

    @FXML
    private Button saveButton;

    private UsersManager usersManager = new UsersManager();
    private Users users; // To hold the user when editing
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
    public void setUser(Users users) {
        this.users = users;

        // Populate fields with existing user data
        if (users != null) {
            usernameField.setText(users.getUsername());
            firstNameField.setText(users.getFirstname());
            lastNameField.setText(users.getLastname());
            emailField.setText(users.getEmail());
            phoneField.setText(users.getPhone());
            rankComboBox.setValue(users.getRankName());
        }
    }

    // Handle Save button
    @FXML
    public void handleSave() {
        // Debugging statement
        System.out.println("handleSave called");

        // Get the password from the input field
        String password = passwordField.getText();
        String username = usernameField.getText();
        String email = emailField.getText();

        // Validate mandatory fields and email format
        if (username.isEmpty() || password.isEmpty() || rankComboBox.getValue() == null || email.isEmpty()) {
            showErrorDialog("Validation Error", "Username, Password, Rank and Email are mandatory fields");
            return;
        }

        // Validate email format using a regular expression
        String emailPattern = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!Pattern.matches(emailPattern, email)) {
            showErrorDialog("Validation Error", "Please enter a valid email address");
            return;
        }

        // Check if the username already exists (unique constraint)
        if (dialogType.equals("add") && usersManager.doesUsernameExist(username)) {
            showErrorDialog("Validation Error", "Username already exists");
            return;
        }

        Task<Boolean> saveTask = new Task<>() {
            @Override
            protected Boolean call() throws Exception {

                boolean success = false;

                if (dialogType.equals("add")) {
                    // Debugging statement
                    System.out.println("Adding user");

                    // Create a new user and add it to the database
                    Users newUser = new Users();
                    newUser.setUsername(username);
                    newUser.setFirstname(firstNameField.getText());
                    newUser.setLastname(lastNameField.getText());
                    newUser.setEmail(email);
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
                    users.setUsername(username);
                    users.setFirstname(firstNameField.getText());
                    users.setLastname(lastNameField.getText());
                    users.setEmail(email);
                    users.setPhone(phoneField.getText());
                    users.setRank(rankComboBox.getValue().equals("Admin") ? 1 : 2);

                    // Use the new password if edited
                    String existingPassword = passwordField.getText();
                    // Passing the new or existing password
                    success = usersManager.editUser(users, existingPassword);

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
                showInfoDialog("User Created", "New user created successfully");
            } else {
                showErrorDialog("Error", "Failed to create user");
            }
            // Close the dialog
            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.close();
        });

        saveTask.setOnFailed(event -> {
            showErrorDialog("Error", "An error occurred while saving the user");
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