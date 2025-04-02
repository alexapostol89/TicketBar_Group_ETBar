package dk.easv.ticketbar2.bll;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

public class QRCodeGenerator {

    // Generate QR Code with the ticket data (not file path)
    public static String generateQRCode(String data, String fileName) throws WriterException, IOException {
        // Generate a unique reference code for the ticket
        String referenceCode = UUID.randomUUID().toString();  // Generates a random unique code (UUID)

        // Define the directory and file path for the QR code image
        String directoryPath = System.getProperty("user.dir") + "/QRCode/";
        File directory = new File(directoryPath);

        // Ensure the directory exists
        if (!directory.exists()) {
            directory.mkdirs(); // Create the directory if it does not exist
        }

        // Define the file path where the image will be saved
        String filePath = directoryPath + referenceCode + ".png";

        // Generate the QR code image
        BitMatrix bitMatrix = new MultiFormatWriter().encode(referenceCode, BarcodeFormat.QR_CODE, 300, 300);
        MatrixToImageWriter.writeToFile(bitMatrix, "png", Paths.get(filePath).toFile());

        System.out.println("Generated QR Code: " + filePath + "at  " + directoryPath);

        // Return the data (ticketData) that was encoded in the QR code, instead of the file path
        return referenceCode;  // Return ticket data (event, ticket type, date)
    }


    public static String generateBarcode(String data, String fileName) throws WriterException, IOException {

        String referenceCode = UUID.randomUUID().toString();

        // Define the directory and file path
        String directoryPath = "dk/easv/ticketbar2/pictures/barcodes/";
        File directory = new File(directoryPath);

        // Ensure the directory exists
        if (!directory.exists()) {
            directory.mkdirs(); // Create the directory if it does not exist
        }

        String filePath = directoryPath + referenceCode + ".png";

        BitMatrix bitMatrix = new MultiFormatWriter().encode(data, BarcodeFormat.CODE_128, 300, 100);
        MatrixToImageWriter.writeToFile(bitMatrix, "png", Paths.get(filePath).toFile());

        return referenceCode; // Return file path so it can be stored in the DB
    }
}
