package dk.easv.ticketbar2.dal.web;


import dk.easv.ticketbar2.be.Users;
import dk.easv.ticketbar2.dal.db.DBConnection;
import dk.easv.ticketbar2.dal.exceptions.EventsException;
import dk.easv.ticketbar2.dal.exceptions.UsersException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.*;


public class UsersDAO {
    private static DBConnection conn = new DBConnection();

    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public static ObservableList<String> getUsersNamesNotAssignedToEvent(int eventID) throws EventsException {
        ObservableList<String> userNames = FXCollections.observableArrayList();

        String sql = "SELECT u.FirstName + ' ' + u.LastName AS FullName " +
                "FROM Users u " +
                "LEFT JOIN UserEvent ue ON u.UserID = ue.UserID AND ue.EventID = ? " +
                "WHERE u.Rank = 2 AND ue.UserID IS NULL";

        try (Connection c = conn.getConnection();
             PreparedStatement stmt = c.prepareStatement(sql)) {

            stmt.setInt(1, eventID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                userNames.add(rs.getString("FullName"));
            }
        } catch (SQLException e) {
            throw new EventsException("Error fetching unassigned event coordinators");
        }

        return userNames;
    }

    public static ObservableList<String> getUsersNamesAssignedToEvent(int eventID) throws EventsException {
        ObservableList<String> assignedUserNames = FXCollections.observableArrayList();

        String sql = "SELECT u.FirstName + ' ' + u.LastName AS FullName " +
                "FROM Users u " +
                "INNER JOIN UserEvent ue ON u.UserID = ue.UserID " +
                "WHERE ue.EventID = ?";

        try (Connection c = conn.getConnection();
             PreparedStatement stmt = c.prepareStatement(sql)) {

            stmt.setInt(1, eventID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                assignedUserNames.add(rs.getString("FullName"));
            }
        } catch (SQLException e) {
            throw new EventsException("Error fetching assigned event coordinators");
        }

        return assignedUserNames;
    }

    public int getUserIDByName(String fullName) throws EventsException {
        String sql = "SELECT UserID FROM Users WHERE FirstName + ' ' + LastName = ?";
        try (Connection c = conn.getConnection();
             PreparedStatement stmt = c.prepareStatement(sql)) {

            stmt.setString(1, fullName); // Correctly passing the full name
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("UserID");
            } else {
                throw new EventsException("User not found: " + fullName);
            }
        } catch (SQLException e) {
            throw new EventsException("Error retrieving UserID for: " + fullName);
        }
        }
        //DAVID METHOD'S
        // Validate User sign or log in
        public Users validateUser(String username, String password) {
            String sql = "SELECT u.UserID, u.Username, u.PasswordHash, u.Rank, ur.rank AS RankName, u.LastLogin FROM Users u " +
                    "JOIN User_rank ur ON u.Rank = ur.id " +
                    "WHERE u.Username = ?";
            String updateLastLoginSQL = "UPDATE Users SET LastLogin = ? WHERE UserID = ?"; // SQL to update the LastLogin field
            try {
                Connection c = conn.getConnection();
                PreparedStatement stmt = c.prepareStatement(sql);

                // Set parameters to prevent SQL injection
                stmt.setString(1, username);

                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    String hashedPassword = rs.getString("PasswordHash");
                    // Use BCrypt to check if the raw password matches the stored hash
                    if (passwordEncoder.matches(password, hashedPassword)) {
                        Users users = new Users();
                        users.setUserid(rs.getInt("UserID"));
                        users.setUsername(rs.getString("Username"));
                        users.setPasswordHash(rs.getString("PasswordHash"));
                        users.setRank(rs.getInt("Rank"));
                        users.setRankName(rs.getString("RankName"));
                        users.setLastLogin(rs.getTimestamp("LastLogin")); // Fetch last login timestamp

                        // Update the lastLogin field to the current timestamp
                        try (PreparedStatement updateStmt = c.prepareStatement(updateLastLoginSQL)) {
                            updateStmt.setTimestamp(1, new Timestamp(System.currentTimeMillis())); // Set current time
                            updateStmt.setInt(2, users.getUserid()); // Set the UserID
                            updateStmt.executeUpdate();
                        }
                        return users;
                    }
                }
            } catch (SQLException e) {
                throw new UsersException(e.getMessage());
            }
            return null;
        }

    // Get all users from the database
    public ObservableList<Users> getAllUsers() {
        ObservableList<Users> userList = FXCollections.observableArrayList();
        String sql = "SELECT u.UserID, u.Username, u.PasswordHash, u.Rank, ur.rank AS RankName, u.FirstName, u.LastName, u.Email, u.Phone, u.CreatedDate, u.LastLogin " +
                "FROM Users u " +
                "JOIN User_rank ur ON u.Rank = ur.id";
        try (Connection c = conn.getConnection();
             PreparedStatement stmt = c.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Users users = new Users();
                users.setUserid(rs.getInt("UserID"));
                users.setUsername(rs.getString("Username"));
                users.setPasswordHash(rs.getString("PasswordHash"));
                users.setRank(rs.getInt("Rank"));
                users.setRankName(rs.getString("RankName"));
                users.setFirstname(rs.getString("FirstName"));
                users.setLastname(rs.getString("LastName"));
                users.setEmail(rs.getString("Email"));
                users.setPhone(rs.getString("Phone"));
                users.setCreatedDate(rs.getString("CreatedDate"));
                users.setLastLogin(rs.getTimestamp("LastLogin"));
                userList.add(users);
            }
        } catch (SQLException e) {
            throw new UsersException(e.getMessage());
        }
        return userList;
    }

    // Get filtered users from the database based on selected filters
    public ObservableList<Users> getFilteredUsers(boolean showAdmin, boolean showCoordinator, String usernameFilter) {
        ObservableList<Users> filteredUsers = FXCollections.observableArrayList();

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

        try (Connection c = conn.getConnection();
             PreparedStatement stmt = c.prepareStatement(sql)) {

            // Set username filter parameter if provided
            if (!usernameFilter.isEmpty()) {
                stmt.setString(1, "%" + usernameFilter + "%");
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Users users = new Users();
                users.setUserid(rs.getInt("UserID"));
                users.setUsername(rs.getString("Username"));
                users.setPasswordHash(rs.getString("PasswordHash"));
                users.setRank(rs.getInt("Rank"));
                users.setRankName(rs.getString("RankName"));
                users.setFirstname(rs.getString("FirstName"));
                users.setLastname(rs.getString("LastName"));
                users.setEmail(rs.getString("Email"));
                users.setPhone(rs.getString("Phone"));
                users.setCreatedDate(rs.getString("CreatedDate"));
                users.setLastLogin(rs.getTimestamp("LastLogin"));
                filteredUsers.add(users);
            }
        } catch (SQLException e) {
            throw new UsersException(e.getMessage());
        }
        return filteredUsers;
    }

    // Add User
    public boolean addUser(Users users) {
        String sql = "INSERT INTO Users (Username, PasswordHash, Rank, FirstName, LastName, Email, Phone) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = conn.getConnection();
             PreparedStatement stmt = c.prepareStatement(sql)) {
            stmt.setString(1, users.getUsername());
            stmt.setString(2, users.getPasswordHash()); // Hashed password
            stmt.setInt(3, users.getRank());
            stmt.setString(4, users.getFirstname());
            stmt.setString(5, users.getLastname());
            stmt.setString(6, users.getEmail());
            stmt.setString(7, users.getPhone());

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
        try (Connection c = conn.getConnection();
             PreparedStatement stmt = c.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0; // If count > 0, username exists
                }
            }
        } catch (SQLException e) {
            throw new UsersException("Database error while checking username existence: " + e.getMessage());
        }
        return false;
    }

    // Edit User
    public boolean editUser(Users users) {
        String sql = "UPDATE Users SET Username = ?, PasswordHash = ?, Rank = ?, FirstName = ?, LastName = ?, " +
                "Email = ?, Phone = ? WHERE UserID = ?";
        try (Connection c = conn.getConnection();
             PreparedStatement stmt = c.prepareStatement(sql)) {
            stmt.setString(1, users.getUsername());
            stmt.setString(2, users.getPasswordHash()); // Hashed password
            stmt.setInt(3, users.getRank());
            stmt.setString(4, users.getFirstname());
            stmt.setString(5, users.getLastname());
            stmt.setString(6, users.getEmail());
            stmt.setString(7, users.getPhone());
            stmt.setInt(8, users.getUserid());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace(); // Print the stack trace for debugging
        }
        return false;
    }

    // Method to delete a user from the database
    public void deleteUser(Users users) {
        String updateEventsSQL = "UPDATE Events SET CoordinatorID = NULL WHERE CoordinatorID = ?";
        String deleteUserSQL = "DELETE FROM Users WHERE UserID = ?";

        try (Connection c = conn.getConnection();
             PreparedStatement updateStmt = c.prepareStatement(updateEventsSQL);
             PreparedStatement deleteStmt = c.prepareStatement(deleteUserSQL)) {

            // Set the UserID of the user in the Events table to NULL
            updateStmt.setInt(1, users.getUserid());
            updateStmt.executeUpdate();

            // Delete the user from the Users table
            deleteStmt.setInt(1, users.getUserid());
            deleteStmt.executeUpdate();
        } catch (SQLException e) {
            throw new UsersException(e);
        }
    }

    // Get a user by ID
    public Users getUserById(int userId) {
        String sql = "SELECT u.UserID, u.Username, u.PasswordHash, u.Rank, ur.rank AS RankName, u.FirstName, u.LastName, u.Email, u.Phone, u.CreatedDate, u.LastLogin " +
                "FROM Users u " +
                "JOIN User_rank ur ON u.Rank = ur.id " +
                "WHERE u.UserID = ?";
        try (Connection c = conn.getConnection();
             PreparedStatement stmt = c.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Users users = new Users();
                users.setUserid(rs.getInt("UserID"));
                users.setUsername(rs.getString("Username"));
                users.setPasswordHash(rs.getString("PasswordHash"));
                users.setRank(rs.getInt("Rank"));
                users.setRankName(rs.getString("RankName"));
                users.setFirstname(rs.getString("FirstName"));
                users.setLastname(rs.getString("LastName"));
                users.setEmail(rs.getString("Email"));
                users.setPhone(rs.getString("Phone"));
                users.setCreatedDate(rs.getString("CreatedDate"));
                users.setLastLogin(rs.getTimestamp("LastLogin"));
                return users;
            }
        } catch (SQLException e) {
            throw new UsersException(e.getMessage());
        }
        return null;
    }
}


