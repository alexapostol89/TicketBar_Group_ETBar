package dk.easv.ticketbar2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import java.io.IOException;

public class CoordinatorController {

    @FXML
    private Pane contentPane;

    @FXML
    public void initialize() {
        try {
            showEvents();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showEvents() throws IOException {
        loadFXML("eventsCoordinator.fxml");
    }

    public void showUsersC() throws IOException {
        loadFXML("usersCoordinator.fxml");
    }

    private void loadFXML(String fxmlFile) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent newContent = loader.load();


        if (fxmlFile.equals("usersCoordinator.fxml")) {
            UsersController controller = loader.getController();
            controller.populateUsers();
        }

        contentPane.getChildren().setAll(newContent);
    }
}
