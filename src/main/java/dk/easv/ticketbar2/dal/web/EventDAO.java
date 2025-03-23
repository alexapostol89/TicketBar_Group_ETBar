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
}
