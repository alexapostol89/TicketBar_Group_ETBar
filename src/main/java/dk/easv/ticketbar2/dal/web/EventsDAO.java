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
                String eventName = rs.getString("EventName");
                String eventDate = rs.getString("StartDateTime");
                String location = rs.getString("Location");
                int createdBy = rs.getInt("CoordinatorID");

                Events eventTable = new Events(eventId, eventName, eventDate, location, createdBy);
                events.add(eventTable);

            }
        } catch (SQLServerException e) {
            throw new RuntimeException(e);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return events;
}
}