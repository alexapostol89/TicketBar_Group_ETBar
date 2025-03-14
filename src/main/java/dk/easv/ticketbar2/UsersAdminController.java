package dk.easv.ticketbar2;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class UsersAdminController {
    @FXML
    private ListView<String> userListA;

    @FXML
    public void initialize() {
        populateUsersA(); // Populate users on load
    }

    public void populateUsersA() {
        if (userListA != null) {
            userListA.getItems().addAll("Alice", "Bob", "Charlie", "David", "Emma");
        }
    }
}