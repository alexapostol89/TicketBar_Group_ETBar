package dk.easv.ticketbar2;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class UsersController {
    @FXML
    private ListView<String> userListC;

    @FXML
    public void initialize() {
        populateUsers(); // Populate users on load
    }

    public void populateUsers() {
        if (userListC != null) {
            userListC.getItems().addAll("Alice", "Bob", "Charlie", "David", "Emma");
        }
    }
}