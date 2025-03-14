package dk.easv.ticketbar2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
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
        loadFXML("eventList.fxml");
    }

    public void showUsersA() throws IOException {
        loadFXML("usersList.fxml");
    }

    private void loadFXML(String fxmlFile) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent newContent = loader.load();


        if (fxmlFile.equals("usersList.fxml")) {
            UsersAdminController controller = loader.getController();
            controller.populateUsersA();
        }

        contentPane.getChildren().setAll(newContent);
    }
}
