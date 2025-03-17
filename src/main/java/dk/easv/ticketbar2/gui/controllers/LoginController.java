package dk.easv.ticketbar2.gui.controllers;

import dk.easv.ticketbar2.bll.UsersManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameTextfield, passwordTextfield;

    private UsersManager usersManager;

    public LoginController() {
        usersManager = new UsersManager();
    }

    public void login(ActionEvent actionEvent) {
        String username = usernameTextfield.getText();
        String password = passwordTextfield.getText();
        // Validate Admin Login
        if (usersManager.validateAdmin(username, password)) {
            try {
                openAdmin();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // Validate Coordinator Login
        else if (usersManager.validateCoordinator(username, password)) {
            try {
                openCoordinator();
            } catch (IOException e) {
                e.printStackTrace();
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
        alert.setTitle("Invalid Login");
        alert.setHeaderText("Invalid Username or Password");
        alert.setContentText("Please check your credentials and try again.");
        alert.showAndWait();
    }

    // Method to open Admin dashboard
    private void openAdmin() throws IOException {
        // Load the Admin UI (FXML or other logic)
        System.out.println("Opening Admin UI...");
    }

    // Method to open Coordinator dashboard
    private void openCoordinator() throws IOException {
        // Load the Coordinator UI (FXML or other logic)
        System.out.println("Opening Coordinator UI...");
    }

    /*
    public TextField usernameTextfield;
    public TextField passwordTextfield;
    private String adminUsername = "a";
    private String adminPassword = "a";
    private String coordinatorUsername = "c";
    private String coordinatorPassword = "c";

    private boolean validateAdmin(String username, String password) {
        if(username.equals(adminUsername) && password.equals(adminPassword)) {
            return true;
        }else {
            return false;
        }
    }

    private boolean validateCoordinator(String username, String password) {
        if(username.equals(coordinatorUsername) && password.equals(coordinatorPassword)) {
            return true;
        }else {
            return false;
        }
    }

    public void login(ActionEvent actionEvent) {
        String username = usernameTextfield.getText();
        String password = passwordTextfield.getText();

        if (validateAdmin(username, password)) {
            try {
                openAdmin();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (validateCoordinator(username, password)) {
            try {
                openCoordinator();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Invalid credentials");
        }
    }

    private void openAdmin() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/dk/easv/ticketbar2/admin-view.fxml"));
        Parent adminRoot = fxmlLoader.load();
        Stage newStage = new Stage();
        newStage.setTitle("Admin Panel");
        newStage.setScene(new Scene(adminRoot));
        newStage.show();

        Stage stage = (Stage) passwordTextfield.getScene().getWindow();
        stage.close();
    }

    private void openCoordinator() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/dk/easv/ticketbar2/coordinator-view.fxml"));
        Parent coordinatorRoot = fxmlLoader.load();
        Stage newStage = new Stage();
        newStage.setTitle("Coordinator Panel");
        newStage.setScene(new Scene(coordinatorRoot));
        newStage.show();

        Stage stage = (Stage) usernameTextfield.getScene().getWindow();
        stage.close();
    }*/
}
