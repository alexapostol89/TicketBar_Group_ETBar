package dk.easv.ticketbar2.dal.web;

import dk.easv.ticketbar2.be.User;
import dk.easv.ticketbar2.dal.db.DBConnection;
import dk.easv.ticketbar2.exceptions.UserException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    private DBConnection connection = new DBConnection();
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Validate User sign or log in
    public User validateUser(String username, String password) {
        String sql = "SELECT u.UserID, u.Username, u.PasswordHash, u.Rank, ur.rank AS RankName FROM Users u " +
                "JOIN User_rank ur ON u.Rank = ur.id " +
                "WHERE u.Username = ?";
        try {
            Connection c = connection.getConnection();
            PreparedStatement stmt = c.prepareStatement(sql);

            // Set parameters to prevent SQL injection
            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String hashedPassword = rs.getString("PasswordHash");
                // Use BCrypt to check if the raw password matches the stored hash
                if (passwordEncoder.matches(password, hashedPassword)) {
                    User user = new User();
                    user.setUserID(rs.getInt("UserID"));
                    user.setUsername(rs.getString("Username"));
                    user.setPasswordHash(rs.getString("PasswordHash"));
                    user.setRank(rs.getInt("Rank"));
                    user.setRankName(rs.getString("RankName"));
                    return user;
                }
            }
        } catch (SQLException e) {
            throw new UserException(e.getMessage());
        }
        return null;
    }

    // Get all users from the database
    public ObservableList<User> getAllUsers() {
        ObservableList<User> userList = FXCollections.observableArrayList();
        String sql = "SELECT u.UserID, u.Username, u.PasswordHash, u.Rank, ur.rank AS RankName, u.FirstName, u.LastName, u.Email, u.Phone, u.CreatedDate, u.LastLogin " +
                "FROM Users u " +
                "JOIN User_rank ur ON u.Rank = ur.id";
        try (Connection c = connection.getConnection();
             PreparedStatement stmt = c.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                User user = new User();
                user.setUserID(rs.getInt("UserID"));
                user.setUsername(rs.getString("Username"));
                user.setPasswordHash(rs.getString("PasswordHash"));
                user.setRank(rs.getInt("Rank"));
                user.setRankName(rs.getString("RankName"));
                user.setFirstName(rs.getString("FirstName"));
                user.setLastName(rs.getString("LastName"));
                user.setEmail(rs.getString("Email"));
                user.setPhone(rs.getString("Phone"));
                user.setCreatedDate(rs.getString("CreatedDate"));
                user.setLastLogin(rs.getString("LastLogin"));
                userList.add(user);
            }
        } catch (SQLException e) {
            throw new UserException(e.getMessage());
        }
        return userList;
    }

    // Get filtered users from the database based on selected filters
    public ObservableList<User> getFilteredUsers(boolean showAdmin, boolean showCoordinator, String usernameFilter) {
        ObservableList<User> filteredUsers = FXCollections.observableArrayList();

        // Base query for fetching users
        String sql = "SELECT u.UserID, u.Username, u.PasswordHash, u.Rank, ur.rank AS RankName, u.FirstName, u.LastName, u.Email, u.Phone, u.CreatedDate, u.LastLogin " +
                "FROM Users u " +
                "JOIN User_rank ur ON u.Rank = ur.id " +
                "WHERE 1=1";

        // Add conditions for filtering by role
        if (showAdmin) {
            sql += " AND ur.rank = 'Admin'";
        }
        if (showCoordinator) {
            sql += " AND ur.rank = 'Coordinator'";
        }
        // Add condition for username search
        if (!usernameFilter.isEmpty()) {
            sql += " AND u.Username LIKE ?";
        }

        try (Connection c = connection.getConnection();
             PreparedStatement stmt = c.prepareStatement(sql)) {

            // Set username filter parameter if provided
            if (!usernameFilter.isEmpty()) {
                stmt.setString(1, "%" + usernameFilter + "%");
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                User user = new User();
                user.setUserID(rs.getInt("UserID"));
                user.setUsername(rs.getString("Username"));
                user.setPasswordHash(rs.getString("PasswordHash"));
                user.setRank(rs.getInt("Rank"));
                user.setRankName(rs.getString("RankName"));
                user.setFirstName(rs.getString("FirstName"));
                user.setLastName(rs.getString("LastName"));
                user.setEmail(rs.getString("Email"));
                user.setPhone(rs.getString("Phone"));
                user.setCreatedDate(rs.getString("CreatedDate"));
                user.setLastLogin(rs.getString("LastLogin"));
                filteredUsers.add(user);
            }
        } catch (SQLException e) {
            throw new UserException(e.getMessage());
        }
        return filteredUsers;
    }

    // Add User
    public boolean addUser(User user) {
        String sql = "INSERT INTO Users (Username, PasswordHash, Rank, FirstName, LastName, Email, Phone) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = connection.getConnection();
             PreparedStatement stmt = c.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPasswordHash()); // Hashed password
            stmt.setInt(3, user.getRank());
            stmt.setString(4, user.getFirstName());
            stmt.setString(5, user.getLastName());
            stmt.setString(6, user.getEmail());
            stmt.setString(7, user.getPhone());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                return true;  // If rows were affected, return true
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // If no rows were affected, return false
    }

    // Check if Username already exists
    public boolean doesUsernameExist(String username) {
        String sql = "SELECT COUNT(*) FROM Users WHERE Username = ?";
        try (Connection c = connection.getConnection();
             PreparedStatement stmt = c.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0; // If count > 0, username exists
                }
            }
        } catch (SQLException e) {
            throw new UserException("Database error while checking username existence: " + e.getMessage());
        }
        return false;
    }

    // Edit User
    public boolean editUser(User user) {
        String sql = "UPDATE Users SET Username = ?, PasswordHash = ?, Rank = ?, FirstName = ?, LastName = ?, " +
                "Email = ?, Phone = ? WHERE UserID = ?";
        try (Connection c = connection.getConnection();
             PreparedStatement stmt = c.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPasswordHash()); // Hashed password
            stmt.setInt(3, user.getRank());
            stmt.setString(4, user.getFirstName());
            stmt.setString(5, user.getLastName());
            stmt.setString(6, user.getEmail());
            stmt.setString(7, user.getPhone());
            stmt.setInt(8, user.getUserID());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace(); // Print the stack trace for debugging
        }
        return false;
    }

    // Method to delete a user from the database
    public void deleteUser(User user) {
        String sql = "DELETE FROM Users WHERE UserID = ?";

        try (Connection c = connection.getConnection();
             PreparedStatement stmt = c.prepareStatement(sql)) {

            // Set the UserID for the user to delete
            stmt.setInt(1, user.getUserID());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new UserException(e.getMessage());
        }
    }

    // Get a user by ID
    public User getUserById(int userId) {
        String sql = "SELECT u.UserID, u.Username, u.PasswordHash, u.Rank, ur.rank AS RankName, u.FirstName, u.LastName, u.Email, u.Phone, u.CreatedDate, u.LastLogin " +
                "FROM Users u " +
                "JOIN User_rank ur ON u.Rank = ur.id " +
                "WHERE u.UserID = ?";
        try (Connection c = connection.getConnection();
             PreparedStatement stmt = c.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setUserID(rs.getInt("UserID"));
                user.setUsername(rs.getString("Username"));
                user.setPasswordHash(rs.getString("PasswordHash"));
                user.setRank(rs.getInt("Rank"));
                user.setRankName(rs.getString("RankName"));
                user.setFirstName(rs.getString("FirstName"));
                user.setLastName(rs.getString("LastName"));
                user.setEmail(rs.getString("Email"));
                user.setPhone(rs.getString("Phone"));
                user.setCreatedDate(rs.getString("CreatedDate"));
                user.setLastLogin(rs.getString("LastLogin"));
                return user;
            }
        } catch (SQLException e) {
            throw new UserException(e.getMessage());
        }
        return null;
    }
}
