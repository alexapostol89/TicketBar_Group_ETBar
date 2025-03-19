package dk.easv.ticketbar2.dal.web;

import dk.easv.ticketbar2.be.Users;
import dk.easv.ticketbar2.dal.db.DBConnection;
import dk.easv.ticketbar2.dal.exceptions.UsersException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsersDAO {
    private DBConnection connection = new DBConnection();

    // Validate User sign or log in
    public Users validateUser(String username, String password) {
        String sql = "SELECT u.UserID, u.Username, u.PasswordHash, u.Rank, ur.rank AS RankName FROM Users u "+
                "JOIN User_rank ur ON u.Rank = ur.id "+
                "WHERE u.Username = ? AND u.PasswordHash = ?";
        try {
            Connection c = connection.getConnection();
            PreparedStatement stmt = c.prepareStatement(sql);

            // Set parameters to prevent SQL injection
            stmt.setString(1, username);
            stmt.setString(2, password); // This will need to be hashed

            ResultSet rs = stmt.executeQuery();

            if(rs.next()){
                Users user = new Users();
                user.setUserID(rs.getInt("UserID"));
                user.setUsername(rs.getString("Username"));
                user.setPasswordHash(rs.getString("PasswordHash"));
                user.setRank(rs.getInt("Rank"));
                user.setRankName(rs.getString("RankName"));
                return user;
            }
        } catch (SQLException e) {
            throw new UsersException(e);
        }
    return null;
    }

    // Get all users from the database
    public ObservableList<Users> getAllUsers() {
        ObservableList<Users> usersList = FXCollections.observableArrayList();
        String sql = "SELECT u.UserID, u.Username, u.Rank, ur.rank AS RankName, u.FirstName, u.LastName, u.Email, u.Phone, u.CreatedDate, u.LastLogin " +
                "FROM Users u " +
                "JOIN User_rank ur ON u.Rank = ur.id";
        try (Connection conn = connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Users user = new Users();
                user.setUserID(rs.getInt("UserID"));
                user.setUsername(rs.getString("Username"));
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
            throw new UsersException(e);
        }
        return usersList;
    }

    // Get a user by ID
    public Users getUserById(int userId) {
        String sql = "SELECT u.UserID, u.Username, u.Rank, ur.rank AS RankName, u.FirstName, u.LastName, u.Email, u.Phone, u.CreatedDate, u.LastLogin " +
                "FROM Users u " +
                "JOIN User_rank ur ON u.Rank = ur.id " +
                "WHERE u.UserID = ?";
        try (Connection conn = connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Users user = new Users();
                user.setUserID(rs.getInt("UserID"));
                user.setUsername(rs.getString("Username"));
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
            throw new UsersException(e);
        }
        return null;
    }
}
