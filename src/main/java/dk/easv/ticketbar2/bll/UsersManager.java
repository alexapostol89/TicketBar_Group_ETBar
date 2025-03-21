package dk.easv.ticketbar2.bll;

import dk.easv.ticketbar2.PasswordUtils;
import dk.easv.ticketbar2.be.Users;
import dk.easv.ticketbar2.dal.web.UsersDAO;
import javafx.collections.ObservableList;

import static dk.easv.ticketbar2.PasswordUtils.hashPassword;

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

    // Add User
    public boolean addUser(Users user, String password) {
        // Hash password using PasswordUtils
        user.setPasswordHash(hashPassword(password));
        return usersDAO.addUser(user);
    }

    // Check if the username already exists
    public boolean doesUsernameExist(String username) {
        return usersDAO.doesUsernameExist(username);
    }

    // Edit User
    public boolean editUser(Users user, String password) {
        if (password != null && !password.isEmpty()) {
            // Hash password using PasswordUtils if provided
            user.setPasswordHash(hashPassword(password));
        }
        return usersDAO.editUser(user);
    }

    // Get user by ID
    public Users getUserById(int userId) {
        return usersDAO.getUserById(userId);
    }

    // Validate user login
    public boolean validateUserLogin(String username, String password) {
        Users user = usersDAO.validateUser(username, password);
        if (user == null) {
            return false; // No user found with that username
        }

        // Use PasswordUtils to check the password
        return PasswordUtils.checkPassword(password, user.getPasswordHash());
    }

    // Get filtered users (delegate to UsersDAO)
    public ObservableList<Users> getFilteredUsers(boolean showAdmin, boolean showCoordinator, String usernameFilter) {
        return usersDAO.getFilteredUsers(showAdmin, showCoordinator, usernameFilter);
    }

    // Method to delete a user
    public void deleteUser(Users user) {
        usersDAO.deleteUser(user);
    }
}
