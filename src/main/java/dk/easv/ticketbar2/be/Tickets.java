package dk.easv.ticketbar2.be;

import java.time.LocalDateTime;

public class Tickets {
    private int ticketId;
    private int eventId;
    private String customerName;
    private String customerEmail;
    private String ticketType;
    private LocalDateTime purchaseDate;
    private String barcode;
    private String qrCode;
    private boolean isScanned;

    // Additional event details
    private String eventName;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String location;
    private String locationGuide;
    private String description;

    // Constructor for Ticket with Event Details
    public Tickets(int ticketId, int eventId, String customerName, String customerEmail, String ticketType,
                   String description, LocalDateTime purchaseDate, String qrCode, String barcode, boolean isScanned) {
        this.ticketId = ticketId;
        this.eventId = eventId;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.ticketType = ticketType;
        this.description = description;
        this.purchaseDate = purchaseDate;
        this.qrCode = qrCode;
        this.barcode = barcode;
        this.isScanned = isScanned;

        // Event details
        this.eventName = eventName;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.location = location;
        this.locationGuide = locationGuide;

    }

    // Getters for all fields


    public String getCustomerName() { return customerName; }
    public String getCustomerEmail() { return customerEmail; }
    public String getTicketType() { return ticketType; }
    public String getDescription() { return description; }
    public String getQrCode() { return qrCode; }
    public String getBarcode() { return barcode; }
    public int getTicketId() { return ticketId; }
    public int getEventId() { return eventId; }
    public LocalDateTime getPurchaseDate() { return purchaseDate; }
    public boolean isScanned( ) {
        return false;
    }
}

