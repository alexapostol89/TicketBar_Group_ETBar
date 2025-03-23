package dk.easv.ticketbar2.dal.web;

import dk.easv.ticketbar2.be.Events;
import dk.easv.ticketbar2.dal.db.DBConnection;
import dk.easv.ticketbar2.dal.exceptions.EventsException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class EventsDAO {
    private DBConnection connection = new DBConnection();

    // Get all events from the database
    public ObservableList<Events> getAllEvents() throws EventsException {
        ObservableList<Events> eventsList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM Events"; // Adjust the query as needed
        try (Connection conn = connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Events event = new Events(
                        rs.getInt("EventID"),
                        rs.getString("EventName"),
                        rs.getString("StartDateTime"),
                        rs.getString("EndDateTime"),
                        rs.getString("Location"),
                        rs.getString("Description"),
                        rs.getString("Notes"),
                        rs.getString("LocationGuide"),
                        rs.getInt("CoordinatorID"),
                        rs.getString("EventImagePath")
                );
                eventsList.add(event);
            }
        } catch (SQLException e) {
            throw new EventsException(e);
        }
        return eventsList;
    }

    // Get an event by ID
    public Events getEventById(int eventId) throws EventsException {
        String sql = "SELECT * FROM Events WHERE EventID = ?";
        try (Connection conn = connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, eventId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Events(
                        rs.getInt("EventID"),
                        rs.getString("EventName"),
                        rs.getString("StartDateTime"),
                        rs.getString("EndDateTime"),
                        rs.getString("Location"),
                        rs.getString("Description"),
                        rs.getString("Notes"),
                        rs.getString("LocationGuide"),
                        rs.getInt("CoordinatorID"),
                        rs.getString("EventImagePath")
                );
            }
        } catch (SQLException e) {
            throw new EventsException(e);
        }
        return null;
    }

    public int saveEvent(String eventName, String imagePath) throws EventsException {
        String insertSql = "INSERT INTO Events (EventName, EventImagePath) VALUES (?, ?)";

        try (Connection conn = connection.getConnection();
             PreparedStatement insertStmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {

            // Insert the event
            insertStmt.setString(1, eventName);
            insertStmt.setString(2, imagePath);
            insertStmt.executeUpdate();

            // Retrieve the auto-generated key (eventID)
            try (ResultSet generatedKeys = insertStmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int eventID = generatedKeys.getInt(1);
                    System.out.println("New event, Id: " + eventID + "added"); // Debugging
                    return eventID; // Returning the generated eventID
                } else {
                }
            }
        } catch (SQLException e) {
        }
        return 0;
    }
}