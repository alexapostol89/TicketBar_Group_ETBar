package dk.easv.ticketbar2.dal.web;

import dk.easv.ticketbar2.dal.db.DBConnection;
import dk.easv.ticketbar2.be.Tickets;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TicketsDAO {

    private final DBConnection connection = new DBConnection();

    public int saveTicket(Tickets ticket) {
        String sql = "INSERT INTO tickets (EventID, CustomerName, CustomerEmail, TicketType, Description, PurchaseDate, QRCode, Barcode, IsScanned) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        int generatedTicketId = -1;

        try (PreparedStatement statement = connection.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, ticket.getEventId());
            statement.setString(2, ticket.getCustomerName());
            statement.setString(3, ticket.getCustomerEmail());
            statement.setString(4, ticket.getTicketType());
            statement.setString(5, ticket.getDescription());
            statement.setObject(6, ticket.getPurchaseDate());
            statement.setString(7, ticket.getQrCode());
            statement.setString(8, ticket.getBarcode());
            statement.setBoolean(9, ticket.isScanned());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        generatedTicketId = generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error while saving ticket to database.");
        }

        return generatedTicketId;
    }

    public Tickets getTicketById(int ticketId) {
        Tickets ticket = null;
        String sql = "SELECT t.TicketID, t.EventID, t.CustomerName, t.CustomerEmail, t.TicketType, t.Description, " +
                "t.PurchaseDate, t.QRCode, t.Barcode, t.IsScanned, " +
                "e.EventName, e.StartDateTime, e.EndDateTime, e.Location, e.LocationGuide " +
                "FROM Tickets t " +
                "JOIN Events e ON t.EventID = e.EventID " +
                "WHERE t.TicketID = ?";

        try (Connection conn = connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, ticketId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                ticket = new Tickets(
                        rs.getInt("TicketID"),
                        rs.getInt("EventID"),
                        rs.getString("CustomerName"),
                        rs.getString("CustomerEmail"),
                        rs.getString("TicketType"),
                        rs.getString("Description"),
                        rs.getTimestamp("PurchaseDate").toLocalDateTime(),
                        rs.getString("QRCode"),
                        rs.getString("Barcode"),
                        rs.getBoolean("IsScanned")
                );
            }
        } catch (SQLException e) {
            Logger.getLogger(TicketsDAO.class.getName()).log(Level.SEVERE, "Database error", e);
        }
        return ticket;
    }


}

