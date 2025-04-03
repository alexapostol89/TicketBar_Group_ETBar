package dk.easv.ticketbar2.gui.controllers;

import dk.easv.ticketbar2.be.Users;
import dk.easv.ticketbar2.bll.UsersManager;
import dk.easv.ticketbar2.dal.web.UsersDAO;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML
    private TextField usernameTextfield, passwordTextfield;

    @FXML
    private Button togglePasswordButton;

    // Method to toggle password visibility
    @FXML
    private void togglePasswordVisibility() {
        // Get the parent AnchorPane
        AnchorPane parent = (AnchorPane) passwordTextfield.getParent();

        // Check if the password field is currently a PasswordField or TextField
        if (passwordTextfield instanceof PasswordField) {
            // Convert to TextField to reveal the password
            TextField textField = new TextField(passwordTextfield.getText());
            textField.setLayoutX(passwordTextfield.getLayoutX());
            textField.setLayoutY(passwordTextfield.getLayoutY());
            textField.setPrefWidth(passwordTextfield.getPrefWidth());

            // Remove the PasswordField and add the TextField
            parent.getChildren().remove(passwordTextfield);
            parent.getChildren().add(textField);

            // Change button text to "Hide"
            togglePasswordButton.setText("Hide");

            // Update the reference to the new TextField
            passwordTextfield = textField;
        } else {
            // Convert back to PasswordField to hide the password
            PasswordField passwordField = new PasswordField();
            passwordField.setText(passwordTextfield.getText());
            passwordField.setLayoutX(passwordTextfield.getLayoutX());
            passwordField.setLayoutY(passwordTextfield.getLayoutY());
            passwordField.setPrefWidth(passwordTextfield.getPrefWidth());

            // Remove the TextField and add the PasswordField
            parent.getChildren().remove(passwordTextfield);
            parent.getChildren().add(passwordField);

            // Change button text to "Show"
            togglePasswordButton.setText("Show");

            // Update the reference to the new PasswordField
            passwordTextfield = passwordField;
        }
    }

    private UsersManager usersManager;

    private UsersDAO usersDAO = new UsersDAO();

    public LoginController() {
        usersManager = new UsersManager();
    }

    public void login(ActionEvent actionEvent) {
        String username = usernameTextfield.getText();
        String password = passwordTextfield.getText();

        System.out.println("Attempting login with username: " + username); // Debug
        Users user = usersDAO.validateUser(username, password);
        if (user != null) {
            System.out.println("User found: " + user.getRankName()); // Debug
        } else {
            System.out.println("User not found or password incorrect"); // Debug
        }



        // Validate Admin Login
        if (usersManager.validateAdmin(username, password)) {
            try {
                openAdmin();
                closeLoginWindow(actionEvent);  // Close the login window after successful login
            } catch (Exception e) {
                showErrorDialog("Error", "An error occurred while opening the admin window");
            }
        }
        // Validate Coordinator Login
        else if (usersManager.validateCoordinator(username, password)) {
            try {
                openCoordinator();
                closeLoginWindow(actionEvent);  // Close the login window after successful login
            } catch (Exception e) {
                showErrorDialog("Error", "An error occurred while opening the Coordinator window");
            }
        }
        // Invalid Credentials
        else {
            showInvalidLoginAlert();
        }
    }

    // Method to show invalid login alert
    private void showInvalidLoginAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid Credentials");
        alert.setHeaderText("Login Failed");
        alert.setContentText("Please check your Username and Password");
        alert.showAndWait();
    }

    // Method to open Admin dashboard
    private void openAdmin() throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dk/easv/ticketbar2/admin-view.fxml"));
            // Cast to TabPane instead of AnchorPane
            TabPane tabPane = (TabPane) loader.load();
            // Open the Admin Panel in a new window
            Stage newStage = new Stage();
            newStage.setTitle("Admin Panel");
            newStage.setScene(new Scene(tabPane));
            newStage.show();
        } catch (Exception e) {
            showErrorDialog("Error", "Failed to open the Admin window");
        }
    }


    // Method to open Coordinator dashboard
    private void openCoordinator() throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dk/easv/ticketbar2/coordinator-view.fxml"));
            Parent root = loader.load();
            Stage newStage = new Stage();
            newStage.setTitle("Coordinator Panel");
            newStage.setScene(new Scene(root));  // THIS LINE WAS MISSING
            newStage.show();
        } catch (Exception e) {
            showErrorDialog("Error", "Failed to open the Coordinator window: " + e.getMessage());
            e.printStackTrace();
        }
    }

        private void closeLoginWindow (ActionEvent actionEvent){
            Stage stage = (Stage) usernameTextfield.getScene().getWindow();
            stage.close();
        }

        /**
         * A helper method for error handling
         * Displays a user-friendly dialog when an error occurs (FXML loading error, invalid credentials, etc.)
         * */
        private void showErrorDialog (String title, String message){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        }
    }

