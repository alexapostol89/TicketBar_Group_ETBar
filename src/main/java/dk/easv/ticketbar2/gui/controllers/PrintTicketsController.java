package dk.easv.ticketbar2.gui.controllers;

import dk.easv.ticketbar2.be.Events;
import dk.easv.ticketbar2.be.Tickets;
import dk.easv.ticketbar2.bll.QRCodeGenerator;
import dk.easv.ticketbar2.bll.EventsManager;
import dk.easv.ticketbar2.dal.exceptions.EventsException;
import javafx.fxml.FXML;


import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.time.LocalDateTime;


public class PrintTicketsController {
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

                // Set time and date (assuming StartDateTime is a LocalDateTime object)
                if (event.getStartDateTime() != null) {
                    lblTime.setText("Time: " + event.getStartDateTime().toString());
                    lblDate.setText("Date: " + event.getStartDateTime().toString());
                } else {
                    lblTime.setText("Time: Unknown");
                    lblDate.setText("Date: Unknown");
                }

                // Set other event details
                lblLocation.setText("Location: " + (event.getLocation() != null ? event.getLocation() : "Unknown"));
                lblDirections.setText("How to get there: " + (event.getLocationGuide() != null ? event.getLocationGuide() : "Unknown"));
                lblDetails.setText("Details: " + (event.getDescription() != null ? event.getDescription() : "No details available"));

                // Now fetch the QR code file path from the ticket object itself
                String qrFilePath = System.getProperty("user.dir") + "/QRCode/";
                System.out.println("Debug test  " + ticket.getQrCode());
                // Ensure the QR code file exists
                File qrFile = new File(qrFilePath);
                if (qrFile.exists()) {
                    // Use toURI().toString() for a valid file URL
                    Image qrImage = new Image(qrFile.toURI().toString());
                    imgQRCode.setImage(qrImage);
                } else {
                    // Handle error if the image file is not found
                    System.out.println("QR Code image not found at: " + qrFilePath);
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
    }
}
