package dk.easv.ticketbar2.dal.web;

import dk.easv.ticketbar2.be.Events;
import dk.easv.ticketbar2.dal.db.DBConnection;
import dk.easv.ticketbar2.exceptions.EventsException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EventsDAO {
    private DBConnection connection = new DBConnection();

    // Get all events from the database
    public ObservableList<Events> getAllEvents() {
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
                        rs.getInt("CoordinatorID")
                );
                eventsList.add(event);
            }
        } catch (SQLException e) {
            throw new EventsException(e);
        }
        return eventsList;
    }

    // Get an event by ID
    public Events getEventById(int eventId) {
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
                        rs.getInt("CoordinatorID")
                );
            }
        } catch (SQLException e) {
            throw new EventsException(e);
        }
        return null;
    }
}
