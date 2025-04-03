package dk.easv.ticketbar2.bll;

import com.google.zxing.WriterException;
import dk.easv.ticketbar2.be.Tickets;
import dk.easv.ticketbar2.dal.web.TicketsDAO;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

public class TicketManager {
    private final TicketsDAO ticketsDAO;

    // Constructor to inject the DAO for flexibility
    public TicketManager(TicketsDAO ticketsDAO) {
        this.ticketsDAO = ticketsDAO;
    }

    // Default constructor to initialize the DAO
    public TicketManager() {
        this(new TicketsDAO());  // Initializes with default DAO implementation
    }

    public int saveTicket(int eventId, String customerName, String customerEmail, String ticketType, String description) {
        LocalDateTime purchaseDate = LocalDateTime.now();
        String barcode = "123456789";
        boolean isScanned = false;

        // Generate UUID once and use it consistently
        String ticketUUID = UUID.randomUUID().toString();
        String qrFileName = ticketUUID + ".png"; // QR code filename will be UUID-based


        try {
            // Pass the UUID to QRCodeGenerator to ensure the same UUID is used for the ticket and the QR code
            QRCodeGenerator.generateQRCode(ticketUUID, qrFileName);  // Use the UUID and filename

        } catch (IOException | WriterException e) {
            e.printStackTrace();
            System.out.println("Failed to generate QR Code.");
            return -1;
        }

        // Store only the file name in the database
        Tickets ticket = new Tickets(0, eventId, customerName, customerEmail, ticketType, description, purchaseDate, qrFileName, barcode, isScanned);

        return ticketsDAO.saveTicket(ticket);
    }

    public Tickets getTickets(int ticketId) {
        return ticketsDAO.getTicketById(ticketId);
    }
}
