package dk.easv.ticketbar2.gui.controllers;

import dk.easv.ticketbar2.be.Tickets;

import javafx.fxml.FXML;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class PrintTicketsController {
    @FXML private Label titleLabel;
    @FXML private Label dateLabel;
    @FXML private Label locationLabel;
    @FXML private Label detailsLabel;
    @FXML private ImageView qrCodeImageView;
    @FXML private ImageView barcodeImageView;

    public void setTicketData(Tickets ticket) {
        if (ticket != null) {
            titleLabel.setText("Event: " + ticket.getEventId());
            dateLabel.setText("Date: " + ticket.getPurchaseDate().toString());
            locationLabel.setText("Location: Example Location");
            detailsLabel.setText("Type: " + ticket.getTicketType());

            // Load QR Code Image
            if (ticket.getQrCode() != null) {
                Image qrImage = new Image("file:" + ticket.getQrCode());
                qrCodeImageView.setImage(qrImage);
            }

            // Load Barcode Image
            if (ticket.getBarcode() != null) {
                Image barcodeImage = new Image("file:" + ticket.getBarcode());
                barcodeImageView.setImage(barcodeImage);

            }
        }
    }
}
