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

    public int saveTicket(int eventId, String customerName, String customerEmail, String ticketType) {
        LocalDateTime purchaseDate = LocalDateTime.now();
        String barcode = "123456789"; // Placeholder barcode
        boolean isScanned = false;

        // Generate QR Code
        String ticketData = UUID.randomUUID().toString();
        String qrCodePath = "C:\\Users\\rauld\\IdeaProjects\\TicketBar_Group_ETBar/QRCode/";

        try {
            qrCodePath = QRCodeGenerator.generateQRCode(qrCodePath, ticketData + System.currentTimeMillis() + ".png");
        } catch (IOException | WriterException e) {
            e.printStackTrace();
            System.out.println("Failed to generate QR Code.");
            return -1;
        }

        // Create ticket object
        Tickets ticket = new Tickets(0, eventId, customerName, customerEmail, ticketType, purchaseDate, qrCodePath, barcode, isScanned);

        // Save ticket to database and return the generated ticket ID
        return ticketsDAO.saveTicket(ticket);
    }

    public Tickets getTickets(int ticketId) {
        return ticketsDAO.getTicketById(ticketId);
    }

}
