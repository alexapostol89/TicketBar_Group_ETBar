package dk.easv.ticketbar2.dal.web;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.ticketbar2.be.Users;
import dk.easv.ticketbar2.dal.db.DBConnection;
import dk.easv.ticketbar2.dal.exceptions.EventsException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsersDAO {
    private static DBConnection conn = new DBConnection();

    public List<Users> getUsers() {
        List<Users> users = new ArrayList<>();
        try {
            Connection c = conn.getConnection();
            String sql = "SELECT * FROM Users";
            System.out.println("connected to users table");
            PreparedStatement statement = c.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {

                int userid = rs.getInt("UserId");
                String username = rs.getString("Username");
                String passwordHash = rs.getString("PasswordHash");
                int rank = rs.getInt("Rank");
                String firstName = rs.getString("FirstName");
                String lastName = rs.getString("LastName");
                String email = rs.getString("Email");
                String lastLogin = rs.getString("LastLogin");
                String createdDate = rs.getString("CreatedDate");
                String phone = rs.getString("Phone");

                Users usersTable = new Users(userid, username, passwordHash, rank, firstName,
                        lastName, email, phone, lastLogin, createdDate);
                users.add(usersTable);

            }
        } catch (SQLServerException e) {
            throw new RuntimeException(e);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return users;
    }

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
            throw new EventsException("Error fetching unassigned event coordinators", e);
        }

        return userNames;
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
            throw new EventsException("Error retrieving UserID for: " + fullName, e);
        }
        }
}

