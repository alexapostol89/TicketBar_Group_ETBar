package dk.easv.ticketbar2.gui.controllers;

import dk.easv.ticketbar2.be.Voucher;
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
import java.text.SimpleDateFormat;
import java.util.Date;

public class PrintVouchersController {

    @FXML
    private AnchorPane voucherContainer;

    @FXML
    private Label voucherTitle;

    @FXML
    private Label voucherSubtitle;

    @FXML
    private Label voucherTerms;

    @FXML
    private Label expirationDate;

    @FXML
    private ImageView imgQRCode;
    @FXML
    private Button printButton;

    public void setVoucherData(Voucher voucher) {
        // Set basic voucher details
        voucherTitle.setText(voucher.getVoucherTitle());
        voucherSubtitle.setText(voucher.getVoucherDescription());
        voucherTerms.setText(voucher.getVoucherTerms());

        // Format and set the expiration date
        Date date = voucher.getExpirationDate(); // Assuming this returns a java.sql.Date or java.util.Date
        String formattedDate = new SimpleDateFormat("dd MMM yyyy").format(date);
        expirationDate.setText("Expires: " + voucher.getExpirationDate());

        // Load QR code image
        String qrCodePath = System.getProperty("user.dir") + "/QRCode/" + voucher.getQrCode();
        File qrFile = new File(qrCodePath);

        if (qrFile.exists()) {
            Image qrImage = new Image(qrFile.toURI().toString(), false);
            imgQRCode.setImage(qrImage);
        } else {
            System.out.println("QR Code image not found at: " + qrCodePath);
            imgQRCode.setImage(new Image("/QRCode/default_image.png"));
        }
    }


    @FXML
    public void printVoucher(ActionEvent event) {
        // Hide the button before printing
        printButton.setVisible(false);

        PrinterJob printerJob = PrinterJob.createPrinterJob();
        if (printerJob != null && printerJob.showPrintDialog(null)) {
            PageLayout pageLayout = printerJob.getJobSettings().getPageLayout();

            double scaleX = pageLayout.getPrintableWidth() / voucherContainer.getBoundsInParent().getWidth();
            double scaleY = pageLayout.getPrintableHeight() / voucherContainer.getBoundsInParent().getHeight();
            double scale = Math.min(scaleX, scaleY); // Maintain aspect ratio

            // Apply scaling
            Scale transform = new Scale(scale, scale);
            voucherContainer.getTransforms().add(transform);

            boolean success = printerJob.printPage(voucherContainer);
            if (success) {
                printerJob.endJob();
            }

            // Clean up (remove the transform so it doesn't affect the UI)
            voucherContainer.getTransforms().remove(transform);
            printButton.setVisible(true);
        }
    }
}
