package dk.easv.ticketbar2.gui.controllers;

import dk.easv.ticketbar2.be.Events;
import dk.easv.ticketbar2.be.Tickets;
import dk.easv.ticketbar2.bll.EventsManager;
import dk.easv.ticketbar2.bll.TicketManager;
import dk.easv.ticketbar2.dal.exceptions.EventsException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;


public class SaveTicketsController {
    private final TicketManager ticketService = new TicketManager();

    @FXML private TextField txtTicketType;
    @FXML private TextField txtCustomerName;
    @FXML private TextField txtCustomerEmail;
    @FXML private Spinner<Integer> spnQuantity;
    @FXML private TextArea txtDescription;
    @FXML private Button btnPrintTickets;

    private final EventsManager eventsManager = new EventsManager();
    private int eventID;


    // Method to set the eventID
    public void setEventID(int eventID) {
        this.eventID = eventID;
        System.out.println("Set eventID in SaveTicketsController: " + eventID);
        loadEventDetails();  // Load event details AFTER setting the event ID
    }

    public int getEventID() {
        return eventID;
    }

    // Initialize spinner
    @FXML
    public void initialize() {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1);
        spnQuantity.setValueFactory(valueFactory);
    }

    // This method will now be called AFTER setEventID is called
    private void loadEventDetails() {
        if (eventID != 0) {
            try {
                Events event = eventsManager.getEventById(eventID);
                System.out.println("Event loaded: " + event.getEventName());
                // Optionally, set event details in UI fields
            } catch (EventsException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void handlePrintTickets(ActionEvent event) throws EventsException {
        // Retrieve values from input fields
        Events eventId = eventsManager.getEventById(eventID); // Implement this based on UI selection
        String customerName = txtCustomerName.getText();
        String customerEmail = txtCustomerEmail.getText();
        String ticketType = txtTicketType.getText();
        String description = txtDescription.getText();

        System.out.println("Handle print tickets for eventID: " + eventID);

        // Validate input fields
        if (customerName.isEmpty() || customerEmail.isEmpty() || ticketType.isEmpty()) {
            showAlert("Missing Information", "Please fill in all fields before printing.");
            return;
        }

        // Save ticket to the database and get generated ticket ID
        int ticketId = ticketService.saveTicket(eventID, customerName, customerEmail, ticketType, description);

        if (ticketId == -1) {
            showAlert("Error", "Failed to create ticket. Please try again.");
            return;
        }

        // Retrieve the saved ticket from the database
        Tickets ticket = ticketService.getTickets(ticketId);

        if (ticket != null) {
            // Open the ticket preview page
            openTicketPage(ticket);
        } else {
            showAlert("Error", "Ticket not found after creation.");
        }
    }




    private void openTicketPage(Tickets ticket) throws EventsException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dk/easv/ticketbar2/ticket.fxml"));
            Scene scene = new Scene(loader.load());


            PrintTicketsController controller = loader.getController();
            controller.setTicketData(ticket);

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Your Ticket");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (EventsException e) {
            throw new RuntimeException(e);
        }
    }

    // Method to load ticket details by ID
    public void loadTicket(int ticketId) {
        Tickets ticket = ticketService.getTickets(ticketId);

        if (ticket != null) {
            txtTicketType.setText(ticket.getTicketType());
            txtCustomerName.setText(ticket.getCustomerName());
            txtCustomerEmail.setText(ticket.getCustomerEmail());
            txtDescription.setText(ticket.getDescription());
        } else {
            showAlert("Ticket Not Found", "The requested ticket could not be found.");
        }
    }

    // Show alert message
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
