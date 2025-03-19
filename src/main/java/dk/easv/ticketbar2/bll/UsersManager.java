package dk.easv.ticketbar2.bll;

import dk.easv.ticketbar2.be.Users;
import dk.easv.ticketbar2.dal.web.UsersDAO;
import javafx.collections.ObservableList;

public class UsersManager {
    private final UsersDAO usersDAO;

    public UsersManager() {
        usersDAO = new UsersDAO();
    }

    // Method to validate Admin login
    public boolean validateAdmin(String username, String password) {
        Users user = usersDAO.validateUser(username, password);
        return user != null && user.getRankName().equals("Admin");
    }

    // Method to validate Coordinator login
    public boolean validateCoordinator(String username, String password) {
        Users user = usersDAO.validateUser(username, password);
        return user != null && user.getRankName().equals("Coordinator");
    }

    // Get all users
    public ObservableList<Users> getAllUsers() {
        return usersDAO.getAllUsers();
    }

    // Get user by ID
    public Users getUserById(int userId) {
        return usersDAO.getUserById(userId);
    }
}
