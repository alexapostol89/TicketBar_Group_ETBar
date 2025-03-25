package dk.easv.ticketbar2.dal.web;

import dk.easv.ticketbar2.be.Event;
import dk.easv.ticketbar2.dal.db.DBConnection;
import dk.easv.ticketbar2.exceptions.EventException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EventDAO {
    private DBConnection connection = new DBConnection();

    // Get all events from the database
    public ObservableList<Event> getAllEvents() {
        ObservableList<Event> eventList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM Events"; // Adjust the query as needed
        try (Connection conn = connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Event event = new Event(
                        rs.getInt("EventID"),
                        rs.getString("EventName"),
                        rs.getString("StartDateTime"),
                        rs.getString("EndDateTime"),
                        rs.getString("Location"),
                        rs.getString("Description"),
                        rs.getString("Notes"),
                        rs.getString("LocationGuide"),
                        rs.getInt("CoordinatorID")
                );
                eventList.add(event);
            }
        } catch (SQLException e) {
            throw new EventException(e);
        }
        return eventList;
    }

    // Get an event by ID
    public Event getEventById(int eventId) {
        String sql = "SELECT * FROM Events WHERE EventID = ?";
        try (Connection conn = connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, eventId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Event(
                        rs.getInt("EventID"),
                        rs.getString("EventName"),
                        rs.getString("StartDateTime"),
                        rs.getString("EndDateTime"),
                        rs.getString("Location"),
                        rs.getString("Description"),
                        rs.getString("Notes"),
                        rs.getString("LocationGuide"),
                        rs.getInt("CoordinatorID")
                );
            }
        } catch (SQLException e) {
            throw new EventException(e);
        }
        return null;
    }

    // Method to get the coordinator's username by their UserID (CoordinatorID in Events)
    public String getCoordinatorUsername(int coordinatorID) {
        String username = null;
        String sql = "SELECT Username FROM Users WHERE UserID = ?";
        try (Connection conn = connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, coordinatorID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    username = rs.getString("Username");
                }
            }
        } catch (SQLException e) {
            throw new EventException(e);
        }
        return username;
    }

    // Method to update the coordinator of the event
    public void updateEventCoordinator(Event event) {
        String sql = "UPDATE Events SET CoordinatorID = ? WHERE EventID = ?";
        try (Connection conn = connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, event.getCoordinatorID()); // Set the new CoordinatorID
            stmt.setInt(2, event.getEventID()); // Set the EventID

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new EventException("An error occurred while updating the coordinator. Please try again later.");
        }
    }

    public void deleteEvent(Event event) {
        String sql = "DELETE FROM Events WHERE EventID = ?";
        try (Connection conn = connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, event.getEventID()); // Set the EventID of the event to delete

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new EventException("An error occurred while deleting the event. Please try again later.");
        }
    }
}
