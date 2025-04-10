package dk.easv.ticketbar2.be;

import java.util.Date;

public class Voucher {

    private int voucherId;
    private int eventId;
    private String voucherTitle;
    private String voucherDescription;
    private String voucherTerms;
    private Date expirationDate; // Store only the date
    private String qrCode;
    private boolean isScanned;

    // Constructor
    public Voucher(int voucherId, int eventId, String voucherTitle, String voucherDescription, String voucherTerms, Date expirationDate, String qrCode, boolean isScanned) {
        this.voucherId = voucherId;
        this.eventId = eventId;
        this.voucherTitle = voucherTitle;
        this.voucherDescription = voucherDescription;
        this.voucherTerms = voucherTerms;
        this.expirationDate = expirationDate;
        this.qrCode = qrCode;
        this.isScanned = false; // Default value for isScanned
    }

    // Getters and Setters
    public int getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(int voucherId) {
        this.voucherId = voucherId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getVoucherTitle() {
        return voucherTitle;
    }

    public void setVoucherTitle(String voucherTitle) {
        this.voucherTitle = voucherTitle;
    }

    public String getVoucherDescription() {
        return voucherDescription;
    }

    public void setVoucherDescription(String voucherDescription) {
        this.voucherDescription = voucherDescription;
    }

    public String getVoucherTerms() {
        return voucherTerms;
    }

    public void setVoucherTerms(String voucherTerms) {
        this.voucherTerms = voucherTerms;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public boolean isScanned() {
        return isScanned;
    }

    public void setScanned(boolean isScanned) {
        this.isScanned = isScanned;
    }
}
