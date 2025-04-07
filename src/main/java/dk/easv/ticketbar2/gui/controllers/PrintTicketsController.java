package dk.easv.ticketbar2.gui.controllers;

import dk.easv.ticketbar2.be.Events;
import dk.easv.ticketbar2.be.Tickets;
import dk.easv.ticketbar2.bll.EventsManager;
import dk.easv.ticketbar2.dal.exceptions.EventsException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;


import javafx.print.PageLayout;
import javafx.print.PrinterJob;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.transform.Scale;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class PrintTicketsController {
    @FXML private AnchorPane ticketContainer;
    @FXML private Button printButton;
    @FXML private Label lblEventName;
    @FXML private Label lblDate;
    @FXML private Label lblTime;
    @FXML private Label lblLocation;
    @FXML private Label lblTicketType;
    @FXML private Label lblDirections;
    @FXML private Label lblDetails;
    @FXML private ImageView imgQRCode;

    private EventsManager eventsManager;

    public PrintTicketsController() {
        // Initialization can be done in initialize() method instead
    }

    // Use the initialize method for further setup or dependency injection
    @FXML
    private void initialize() {
        this.eventsManager = new EventsManager(); // Initialize EventsManager
    }

    public void setTicketData(Tickets ticket) throws EventsException {
        if (ticket != null) {
            // Fetch the event details based on the eventId in the ticket
            Events event = eventsManager.getEventById(ticket.getEventId());

            if (event != null) {
                // Set the event details in the labels
                lblEventName.setText(event.getEventName());
                lblTicketType.setText(ticket.getTicketType());

                // Define correct format for date-time parsing
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
                LocalDateTime dateTime = LocalDateTime.parse(event.getStartDateTime(), formatter);

                // Format and set date and time labels
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

                lblTime.setText("Time: " + dateTime.format(timeFormatter));
                lblDate.setText("Date: " + dateTime.format(dateFormatter));
            } else {
                lblTime.setText("Time: Unknown");
                lblDate.setText("Date: Unknown");
            }

            // Set other event details
            lblLocation.setText("Location: " + (event.getLocation() != null ? event.getLocation() : "Unknown"));
            lblDirections.setText("How to get there: " + (event.getLocationGuide() != null ? event.getLocationGuide() : "Unknown"));
            lblDetails.setText("Details: " + (ticket.getDescription() != null ? ticket.getDescription() : "No details available"));

            // Now fetch the QR code file path from the ticket object itself
            String qrCodePath = System.getProperty("user.dir") + "/QRCode/" + ticket.getQrCode();
            File qrFile = new File(qrCodePath);

            if (qrFile.exists()) {
                // Use toURI().toString() for a valid file URL
                Image qrImage = new Image(qrFile.toURI().toString(), false);
                imgQRCode.setImage(qrImage);
            } else {
                // Handle error if the image file is not found
                System.out.println("QR Code image not found at: " + qrCodePath);
                // Optionally set a default image
                imgQRCode.setImage(new Image("/QRCode/default_image.png"));
            }

        } else {
            // Handle case when the event is not found
            lblEventName.setText("Event not found!");
            lblDate.setText("Date: Unknown");
            lblTime.setText("Time: Unknown");
            lblLocation.setText("Location: Unknown");
            lblDirections.setText("Directions: Unknown");
            lblDetails.setText("Details: Unknown");
        }
    }

    @FXML
    public void printTicket(ActionEvent actionEvent) {
        // Hide the button before printing
        printButton.setVisible(false);

        PrinterJob printerJob = PrinterJob.createPrinterJob();
        if (printerJob != null && printerJob.showPrintDialog(null)) {
            PageLayout pageLayout = printerJob.getJobSettings().getPageLayout();

            double scaleX = pageLayout.getPrintableWidth() / ticketContainer.getBoundsInParent().getWidth();
            double scaleY = pageLayout.getPrintableHeight() / ticketContainer.getBoundsInParent().getHeight();
            double scale = Math.min(scaleX, scaleY); // Maintain aspect ratio

            // Apply scaling
            Scale transform = new Scale(scale, scale);
            ticketContainer.getTransforms().add(transform);

            boolean success = printerJob.printPage(ticketContainer);
            if (success) {
                printerJob.endJob();
            }

            // Clean up (remove the transform so it doesn't affect the UI)
            ticketContainer.getTransforms().remove(transform);
            printButton.setVisible(true);
        }
    }


}
