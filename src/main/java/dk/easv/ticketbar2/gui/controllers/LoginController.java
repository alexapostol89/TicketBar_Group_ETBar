package dk.easv.ticketbar2.gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML
    private TextField usernameTextfield;

    @FXML
    private PasswordField passwordTextfield;

    @FXML
    private TextField textFieldPassword;

    @FXML
    private Button togglePasswordButton;

    private boolean isPasswordVisible = false;

    @FXML
    public void initialize() {
        textFieldPassword.setVisible(false);
        textFieldPassword.setManaged(false);
    }

    @FXML
    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            // Switch to PasswordField (hide text)
            passwordTextfield.setText(textFieldPassword.getText());
            passwordTextfield.setVisible(true);
            passwordTextfield.setManaged(true);
            textFieldPassword.setVisible(false);
            textFieldPassword.setManaged(false);
        } else {
            // Switch to TextField (show text)
            textFieldPassword.setText(passwordTextfield.getText());
            textFieldPassword.setVisible(true);
            textFieldPassword.setManaged(true);
            passwordTextfield.setVisible(false);
            passwordTextfield.setManaged(false);
        }
        isPasswordVisible = !isPasswordVisible;
    }

    @FXML
    public void login(ActionEvent actionEvent) {
        String username = usernameTextfield.getText();
        String password = isPasswordVisible ? textFieldPassword.getText() : passwordTextfield.getText();

        if (validateAdmin(username, password)) {
            openAdmin();
        } else if (validateCoordinator(username, password)) {
            openCoordinator();
        } else {
            System.out.println("Invalid credentials");
        }
    }

    private boolean validateAdmin(String username, String password) {
        return username.equals("a") && password.equals("a");
    }

    private boolean validateCoordinator(String username, String password) {
        return username.equals("c") && password.equals("c");
    }

    private void openAdmin() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/dk/easv/ticketbar2/admin-view.fxml"));
            Parent adminRoot = fxmlLoader.load();
            Stage newStage = new Stage();
            newStage.setTitle("Admin Panel");
            newStage.setScene(new Scene(adminRoot));
            newStage.show();

            ((Stage) usernameTextfield.getScene().getWindow()).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openCoordinator() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/dk/easv/ticketbar2/coordinator-view.fxml"));
            Parent coordinatorRoot = fxmlLoader.load();
            Stage newStage = new Stage();
            newStage.setTitle("Coordinator Panel");
            newStage.setScene(new Scene(coordinatorRoot));
            newStage.show();
            newStage.setWidth(1306);
            newStage.setHeight(800);

            ((Stage) usernameTextfield.getScene().getWindow()).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
