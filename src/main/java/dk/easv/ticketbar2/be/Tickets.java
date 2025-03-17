package dk.easv.ticketbar2.be;

public class Tickets {
    private int Ticket_id;
    private int Event_id;
    private int Customer_id;
    private String Ticket_type;
    private String Purchase_date;
    private String Barcode;
    private String QRCode;
    private boolean IsScanned;

    public Tickets(int ticket_id, int event_id, int customer_id, String ticket_type, String purchase_date, String barcode, String QRCode, boolean isScanned) {
        Ticket_id = ticket_id;
        Event_id = event_id;
        Customer_id = customer_id;
        Ticket_type = ticket_type;
        Purchase_date = purchase_date;
        Barcode = barcode;
        this.QRCode = QRCode;
        IsScanned = isScanned;
    }

    public int getTicket_id() {
        return Ticket_id;
    }

    public void setTicket_id(int ticket_id) {
        Ticket_id = ticket_id;
    }

    public int getEvent_id() {
        return Event_id;
    }

    public void setEvent_id(int event_id) {
        Event_id = event_id;
    }

    public int getCustomer_id() {
        return Customer_id;
    }

    public void setCustomer_id(int customer_id) {
        Customer_id = customer_id;
    }

    public String getTicket_type() {
        return Ticket_type;
    }

    public void setTicket_type(String ticket_type) {
        Ticket_type = ticket_type;
    }

    public String getPurchase_date() {
        return Purchase_date;
    }

    public void setPurchase_date(String purchase_date) {
        Purchase_date = purchase_date;
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
