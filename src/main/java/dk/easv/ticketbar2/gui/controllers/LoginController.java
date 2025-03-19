package dk.easv.ticketbar2.gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
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
        // Load the admin FXML
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
    }
}
