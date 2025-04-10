package dk.easv.ticketbar2.gui.controllers;

import dk.easv.ticketbar2.be.Events;
import dk.easv.ticketbar2.bll.EventsManager;
import dk.easv.ticketbar2.bll.VoucherManager;
import dk.easv.ticketbar2.be.Voucher;
import dk.easv.ticketbar2.dal.exceptions.EventsException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

public class SaveVouchersController {

    private final VoucherManager voucherManager = new VoucherManager(); // Business logic layer

    @FXML
    private TextField voucherTitle;

    @FXML
    private TextArea voucherDescription;

    @FXML
    private TextArea voucherTerms;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Button displayVoucher;

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
    public void displayVoucher(ActionEvent event) {
        try {
            String title = voucherTitle.getText();
            String description = voucherDescription.getText();
            String terms = voucherTerms.getText();
            LocalDate expirationLocalDate = datePicker.getValue();

            if (title.isEmpty() || description.isEmpty() || terms.isEmpty() || expirationLocalDate == null) {
                showAlert("Missing Information", "Please fill in all fields.");
                return;
            }

            int voucherId = voucherManager.saveVoucher(eventID,title, description, terms, Date.valueOf(expirationLocalDate));

            if (voucherId == -1) {
                showAlert("Error", "Failed to create voucher. Please try again.");
                return;
            }

            Voucher voucher = voucherManager.getVouchers(voucherId);

            if (voucher != null) {
                openVoucherPage(voucher);
            } else {
                showAlert("Error", "Voucher not found after creation.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Exception", "Something went wrong. Check console for details.");
        }
    }

    private void openVoucherPage(Voucher voucher) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dk/easv/ticketbar2/voucher.fxml"));
            Scene scene = new Scene(loader.load());

            PrintVouchersController controller = loader.getController();
            controller.setVoucherData(voucher);

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Voucher Details");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
