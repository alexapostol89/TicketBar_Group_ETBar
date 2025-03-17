package dk.easv.ticketbar2.be;

public class Tickets {
    private int TicketID;
    private int EventID;
    private String CustomerName;
    private String CustomerEmail;
    private String TicketType;
    private String PurchaseDate;
    private String Barcode;
    private String QRCode;
    private boolean IsScanned;

    public Tickets(int ticketID, int eventID, String customerName, String customerEmail, String ticketType, String purchaseDate, String barcode, String QRCode, boolean isScanned) {
        TicketID = ticketID;
        EventID = eventID;
        CustomerName = customerName;
        CustomerEmail = customerEmail;
        TicketType = ticketType;
        PurchaseDate = purchaseDate;
        Barcode = barcode;
        this.QRCode = QRCode;
        IsScanned = isScanned;
    }

    public int getTicketID() {
        return TicketID;
    }

    public void setTicketID(int ticketID) {
        TicketID = ticketID;
    }

    public int getEventID() {
        return EventID;
    }

    public void setEventID(int eventID) {
        EventID = eventID;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getCustomerEmail() {
        return CustomerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        CustomerEmail = customerEmail;
    }

    public String getTicketType() {
        return TicketType;
    }

    public void setTicketType(String ticketType) {
        TicketType = ticketType;
    }

    public String getPurchaseDate() {
        return PurchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        PurchaseDate = purchaseDate;
    }

    public String getBarcode() {
        return Barcode;
    }

    public void setBarcode(String barcode) {
        Barcode = barcode;
    }

    public String getQRCode() {
        return QRCode;
    }

    public void setQRCode(String QRCode) {
        this.QRCode = QRCode;
    }

    public boolean isScanned() {
        return IsScanned;
    }

    public void setScanned(boolean scanned) {
        IsScanned = scanned;
    }
}
