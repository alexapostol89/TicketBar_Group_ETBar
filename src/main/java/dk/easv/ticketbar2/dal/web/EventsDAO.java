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

    public int saveEvent(String eventName, String imagePath, String startDate, String endDate, String location, String description, String notes, String locationGuide, int coordinator) throws EventsException {
        String insertSql = "INSERT INTO Events (EventName, EventImagePath, StartDateTime, EndDateTime, Location, Description, Notes, LocationGuide, CoordinatorID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = connection.getConnection();
             PreparedStatement insertStmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {

            // Insert the event info
            insertStmt.setString(1, eventName);
            insertStmt.setString(2, imagePath);
            insertStmt.setString(3, startDate);
            insertStmt.setString(4, endDate);
            insertStmt.setString(5, location);
            insertStmt.setString(6, description);
            insertStmt.setString(7, notes);
            insertStmt.setString(8, locationGuide);
            insertStmt.setInt(9, coordinator);


            insertStmt.executeUpdate();

            try (ResultSet generatedKeys = insertStmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int eventID = generatedKeys.getInt(1);
                    System.out.println("Event " + eventName + "added to the database"); // Debugging
                    return eventID; // Returning the generated eventID
                } else {
                }
            }
        } catch (SQLException e) {
        }
        return 0;
    }

    public boolean deleteEvent(int eventId) {

        String query = "DELETE FROM Events WHERE EventID = ?";
        try (Connection conn = connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, eventId);
            System.out.println("Executing DELETE for event ID: " + eventId); // Debugging
            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Event deleted successfully: ID = " + eventId);
                return true;
            } else {
                System.out.println("No event found with ID = " + eventId);
                return false;
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage()); // Print error message
            e.printStackTrace(); // Print full stack trace
            return false;
        }
    }

    public boolean updateEvent(Events event) {
        String sql = "UPDATE Events SET EventName = ?, EventImagePath = ?, StartDateTime = ?, " +
                "EndDateTime = ?, Location = ?, Description = ?, Notes = ?, " +
                "LocationGuide = ? WHERE EventID = ?";

        try (Connection conn = connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, event.getEventName());
            stmt.setString(2, event.getEventImagePath());
            stmt.setString(3, event.getStartDateTime());
            stmt.setString(4, event.getEndDateTime());
            stmt.setString(5, event.getLocation());
            stmt.setString(6, event.getDescription());
            stmt.setString(7, event.getNotes());
            stmt.setString(8, event.getLocationGuide());
            stmt.setInt(9, event.getEventID());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

