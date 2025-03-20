package dk.easv.ticketbar2.dal.web;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.ticketbar2.be.Events;
import dk.easv.ticketbar2.dal.db.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EventsDAO {
    private DBConnection conn = new DBConnection();

    public List<Events> getEvents() {
        List<Events> events = new ArrayList<>();
        try {
            Connection c = conn.getConnection();
            System.out.println("database connected successful");
            String sql = "SELECT * FROM Events";
            PreparedStatement statement = c.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {

                int eventId = rs.getInt("EventID");
                int createdBy = rs.getInt("CoordinatorID");
                String eventName = rs.getString("EventName");
                String eventDate = rs.getString("StartDateTime");
                String endDate = rs.getString("EndDateTime");
                String location = rs.getString("Location");
                String eventDescription = rs.getString("Description");
                String notes = rs.getString("Notes");
                String locationGuide = rs.getString("LocationGuide");
                String eventImagePath = rs.getString("EventImagePath");


                Events eventsTable = new Events (eventId, createdBy,eventName, eventDate, endDate, location, eventDescription, notes, locationGuide, eventImagePath);
                events.add(eventsTable);



            }
        } catch (SQLServerException e) {
            throw new RuntimeException(e);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return events;
}

    public void saveEvent(String eventName, String imagePath) {
        try {
            Connection c = conn.getConnection();
            String sql = "INSERT INTO Events (EventName, EventImagePath) VALUES (?, ?)";
            PreparedStatement statement = c.prepareStatement(sql);
            statement.setString(1, eventName);
            statement.setString(2, imagePath);
            statement.executeUpdate();
            System.out.println("Event saved successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}