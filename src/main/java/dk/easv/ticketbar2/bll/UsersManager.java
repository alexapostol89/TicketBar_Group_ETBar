package dk.easv.ticketbar2.bll;

import dk.easv.ticketbar2.be.Users;
import dk.easv.ticketbar2.dal.exceptions.EventsException;
import dk.easv.ticketbar2.dal.web.UsersDAO;
import javafx.collections.ObservableList;
import dk.easv.ticketbar2.utils.PasswordUtils;
import static dk.easv.ticketbar2.utils.PasswordUtils.hashPassword;


public class UsersManager {
    private final UsersDAO usersDAO = new UsersDAO();

    public ObservableList<String> getUsersNamesNotAssignedToEvent(int eventID) throws EventsException {
        return usersDAO.getUsersNamesNotAssignedToEvent(eventID);
    }

    public ObservableList<String> getUsersNamesAssignedToEvent(int eventID) throws EventsException {
        return usersDAO.getUsersNamesAssignedToEvent(eventID);
    }

    public int getUserIDByName(String fullName) throws EventsException {
        return usersDAO.getUserIDByName(fullName); // Calls the DAO method properly
    }

    public String getFullNameByUserID(int userID) throws EventsException {
        return usersDAO.getFullNameByUserID(userID);
    }



    //DAVID METHOD'S
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
    public boolean addUser(Users users, String password) {
        // Hash password using PasswordUtils
        users.setPasswordHash(hashPassword(password));
        return usersDAO.addUser(users);
    }

    // Check if the username already exists
    public boolean doesUsernameExist(String username) {
        return usersDAO.doesUsernameExist(username);
    }

    // Edit User
    public boolean editUser(Users users, String password) {
        if (password != null && !password.isEmpty()) {
            // Hash password using PasswordUtils if provided
            users.setPasswordHash(hashPassword(password));
        }
        return usersDAO.editUser(users);
    }

    // Get user by ID
    public Users getUserById(int userId) {
        return usersDAO.getUserById(userId);
    }

    // Validate user login
    public boolean validateUserLogin(String username, String password) {
        Users users = usersDAO.validateUser(username, password);
        if (users == null) {
            return false; // No user found with that username
        }

        // Use PasswordUtils to check the password
        return PasswordUtils.checkPassword(password, users.getPasswordHash());
    }

    // Get filtered users (delegate to UsersDAO)
    public ObservableList<Users> getFilteredUsers(boolean showAdmin, boolean showCoordinator, String usernameFilter) {
        return usersDAO.getFilteredUsers(showAdmin, showCoordinator, usernameFilter);
    }

    // Method to delete a user
    public void deleteUser(Users users) {
        usersDAO.deleteUser(users);
    }
}
