package dk.easv.ticketbar2.gui.controllers;

import dk.easv.ticketbar2.bll.UserManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField usernameTextfield, passwordTextfield;

    private UserManager userManager;

    public LoginController() {
        userManager = new UserManager();
    }

    public void login(ActionEvent actionEvent) {
        String username = usernameTextfield.getText();
        String password = passwordTextfield.getText();

        // Validate Admin Login
        if (userManager.validateAdmin(username, password)) {
            try {
                openAdmin();
                closeLoginWindow(actionEvent);  // Close the login window after successful login
            } catch (Exception e) {
                showErrorDialog("Error","An error occurred while opening the admin window");
            }
        }
        // Validate Coordinator Login
        else if (userManager.validateCoordinator(username, password)) {
            try {
                openCoordinator();
                closeLoginWindow(actionEvent);  // Close the login window after successful login
            } catch (Exception e) {
                showErrorDialog("Error","An error occurred while opening the admin window");
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
            showErrorDialog("Error","Failed to open the Admin window");
        }
    }

    // Method to open Coordinator dashboard
    private void openCoordinator() throws Exception {
        // Load the Coordinator UI (FXML or other logic)
        System.out.println("Opening Coordinator UI...");
    }

    private void closeLoginWindow(ActionEvent actionEvent){
        Stage stage = (Stage) usernameTextfield.getScene().getWindow();
        stage.close();
    }

    /**
     *
     * A helper method for error handling
     * Displays a user-friendly dialog when an error occurs (FXML loading error, invalid credentials, etc.)
     * You no longer need to import IOException
     */
    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
