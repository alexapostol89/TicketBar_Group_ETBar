package dk.easv.ticketbar2.dal.web;

import dk.easv.ticketbar2.be.Users;
import dk.easv.ticketbar2.dal.db.DBConnection;
import dk.easv.ticketbar2.dal.exceptions.UsersException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsersDAO {
    private DBConnection connection = new DBConnection();
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Validate User sign or log in
    public Users validateUser(String username, String password) {
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
                    Users user = new Users();
                    user.setUserID(rs.getInt("UserID"));
                    user.setUsername(rs.getString("Username"));
                    user.setPasswordHash(rs.getString("PasswordHash"));
                    user.setRank(rs.getInt("Rank"));
                    user.setRankName(rs.getString("RankName"));
                    return user;
                }
            }
        } catch (SQLException e) {
            throw new UsersException(e.getMessage());
        }
        return null;
    }

    // Get all users from the database
    public ObservableList<Users> getAllUsers() {
        ObservableList<Users> usersList = FXCollections.observableArrayList();
        String sql = "SELECT u.UserID, u.Username, u.PasswordHash, u.Rank, ur.rank AS RankName, u.FirstName, u.LastName, u.Email, u.Phone, u.CreatedDate, u.LastLogin " +
                "FROM Users u " +
                "JOIN User_rank ur ON u.Rank = ur.id";
        try (Connection c = connection.getConnection();
             PreparedStatement stmt = c.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Users user = new Users();
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
                usersList.add(user);
            }
        } catch (SQLException e) {
            throw new UsersException(e.getMessage());
        }
        return usersList;
    }

    // Add User
    public boolean addUser(Users user) {
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

    // Edit User
    public boolean editUser(Users user) {
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

    // Get a user by ID
    public Users getUserById(int userId) {
        String sql = "SELECT u.UserID, u.Username, u.PasswordHash, u.Rank, ur.rank AS RankName, u.FirstName, u.LastName, u.Email, u.Phone, u.CreatedDate, u.LastLogin " +
                "FROM Users u " +
                "JOIN User_rank ur ON u.Rank = ur.id " +
                "WHERE u.UserID = ?";
        try (Connection c = connection.getConnection();
             PreparedStatement stmt = c.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Users user = new Users();
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
            throw new UsersException(e.getMessage());
        }
        return null;
    }
}
