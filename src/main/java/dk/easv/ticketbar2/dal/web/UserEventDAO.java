package dk.easv.ticketbar2.dal.web;

import dk.easv.ticketbar2.dal.db.DBConnection;
import dk.easv.ticketbar2.dal.exceptions.EventsException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserEventDAO {

    private DBConnection connection = new DBConnection();

    public void insertUserEvent(int userID, int eventID) throws EventsException {
        String sql = "INSERT INTO UserEvent (UserID, EventID) VALUES (?, ?)";

        try (Connection conn = connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userID);
            stmt.setInt(2, eventID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new EventsException("Error inserting UserEvent: " + e.getMessage(), e);
        }
    }
}

