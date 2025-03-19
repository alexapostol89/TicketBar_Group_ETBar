package dk.easv.ticketbar2.gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminController {

    @FXML
    private Pane contentPane;

    @FXML
    public void initialize() {
        try {
            showEventsA();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showEventsA() throws IOException {
        loadFXML("/dk/easv/ticketbar2/event-list.fxml");
    }

    public void showUsersA() throws IOException {
        loadFXML("/dk/easv/ticketbar2/user-list.fxml");
    }

    private void loadFXML(String fxmlFile) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent newContent = loader.load();

        if (fxmlFile.equals("user-list.fxml")) {
            UsersAdminController controller = loader.getController();
            controller.populateUsersA();
        }

        contentPane.getChildren().setAll(newContent);
    }
    @FXML
    private void logout(ActionEvent event) {
        try{
            //Load Login screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dk/easv/ticketbar2/login-view.fxml"));
            Parent root = loader.load();

            //Closes actual stage
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            Stage stage = new Stage();
            stage.setTitle("Login");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
