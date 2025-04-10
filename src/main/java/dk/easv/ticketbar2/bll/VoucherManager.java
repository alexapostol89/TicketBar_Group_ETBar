package dk.easv.ticketbar2.bll;

import com.google.zxing.WriterException;
import dk.easv.ticketbar2.be.Voucher;
import dk.easv.ticketbar2.dal.web.VouchersDAO;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.UUID;

public class VoucherManager {
    private final VouchersDAO vouchersDAO;

    // Constructor to inject the DAO for flexibility
    public VoucherManager(VouchersDAO vouchersDAO) {
        this.vouchersDAO = vouchersDAO;
    }

    // Default constructor to initialize the DAO
    public VoucherManager() {
        this(new VouchersDAO());  // Initializes with default DAO implementation
    }

    // Method to save a voucher and handle its QR code generation
    public int saveVoucher(int eventId, String voucherTitle, String voucherDescription, String voucherTerms, Date expirationDate) {
        String qrFileName;
        boolean isScanned = false;
        try {
            // Generate UUID for QR code file name
            String ticketUUID = UUID.randomUUID().toString();
            qrFileName = ticketUUID + ".png";

            // Generate QR code and save the file
            QRCodeGenerator.generateQRCode(ticketUUID, qrFileName);
        } catch (IOException | WriterException e) {
            e.printStackTrace();
            System.out.println("Failed to generate QR Code.");
            return -1; // Return an error code if QR code generation fails
        }

        // Create the Voucher object
        Voucher voucher = new Voucher(0, eventId, voucherTitle, voucherDescription, voucherTerms, expirationDate, qrFileName, isScanned);

        try {
            // Save voucher to the database and return the generated ID
            return vouchersDAO.saveVoucher(voucher);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to save voucher to the database.");
            return -1; // Return an error code if saving fails
        }
    }

    // Method to fetch a voucher by its ID
    public Voucher getVouchers(int voucherId) {
            return vouchersDAO.getVouchersById(voucherId);
    }
}
