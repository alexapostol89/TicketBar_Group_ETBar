package dk.easv.ticketbar2.dal.web;

import dk.easv.ticketbar2.dal.db.DBConnection;
import dk.easv.ticketbar2.be.Voucher;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VouchersDAO {

    private final DBConnection connection = new DBConnection();

    // Method to save a new voucher into the database
    public int saveVoucher(Voucher voucher) throws SQLException {
        String sql = "INSERT INTO Vouchers (Voucher_title, Voucher_subtitle, Voucher_terms, expiration_date, QRCode, Event_ID, IsScanned) VALUES (?, ?, ?, ?, ?, ?, ?)";
        int generatedVoucherId = -1;

        try (PreparedStatement pstmt = connection.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, voucher.getVoucherTitle());
            pstmt.setString(2, voucher.getVoucherDescription());
            pstmt.setString(3, voucher.getVoucherTerms());
            pstmt.setDate(4, new java.sql.Date(voucher.getExpirationDate().getTime())); // Correct Date conversion
            pstmt.setString(5, voucher.getQrCode());
            pstmt.setInt(6, voucher.getEventId());
            pstmt.setBoolean(7, voucher.isScanned()); // Bind the IsScanned column as a boolean

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1); // Return the auto-generated voucher_id
                    }
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(VouchersDAO.class.getName()).log(Level.SEVERE, "Error saving voucher to database.", e);
        }
        return generatedVoucherId;
    }

    // Method to fetch a voucher by its ID
    public Voucher getVouchersById(int voucherId) {
        Voucher voucher = null;
        String sql = "SELECT v.Voucher_ID, v.Voucher_title, v.Voucher_subtitle, v.Voucher_terms, " +
                "v.expiration_date, v.QRCode, v.IsScanned, v.event_ID, e.EventName " +
                "FROM Vouchers v " +
                "JOIN Events e ON v.event_ID = e.EventID " +
                "WHERE v.Voucher_ID = ?";

        try (Connection conn = connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, voucherId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                voucher = new Voucher(
                        rs.getInt("Voucher_ID"),
                        rs.getInt("event_ID"),
                        rs.getString("Voucher_title"),
                        rs.getString("Voucher_subtitle"),
                        rs.getString("Voucher_terms"),
                        rs.getDate("expiration_date"),
                        rs.getString("QRCode"),
                        rs.getBoolean("IsScanned")
                );
            }
        } catch (SQLException e) {
            Logger.getLogger(VouchersDAO.class.getName()).log(Level.SEVERE, "Database error", e);
        }
        return voucher;
    }
}
