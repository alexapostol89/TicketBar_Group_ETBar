package dk.easv.ticketbar2.bll;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class QRCodeGenerator {

    // Generate QR Code with the ticket data (using the passed UUID for consistency)
    public static void generateQRCode(String uuid, String fileName) throws WriterException, IOException {
        // Define the directory and file path for the QR code image
        String directoryPath = System.getProperty("user.dir") + "/QRCode/";
        File directory = new File(directoryPath);

        // Ensure the directory exists
        if (!directory.exists()) {
            directory.mkdirs(); // Create the directory if it does not exist
        }

        // Define the file path where the image will be saved
        String filePath = directoryPath + fileName;

        // Generate the QR code image
        BitMatrix bitMatrix = new MultiFormatWriter().encode(uuid, BarcodeFormat.QR_CODE, 300, 300);
        MatrixToImageWriter.writeToFile(bitMatrix, "png", Paths.get(filePath).toFile());

    }

    public static String generateBarcode(String data, String fileName) throws WriterException, IOException {

        // Define the directory and file path for the barcode
        String directoryPath = "dk/easv/ticketbar2/pictures/barcodes/";
        File directory = new File(directoryPath);

        // Ensure the directory exists
        if (!directory.exists()) {
            directory.mkdirs(); // Create the directory if it does not exist
        }

        String filePath = directoryPath + fileName;

        BitMatrix bitMatrix = new MultiFormatWriter().encode(data, BarcodeFormat.CODE_128, 300, 100);
        MatrixToImageWriter.writeToFile(bitMatrix, "png", Paths.get(filePath).toFile());

        return fileName; // Return the file name for saving to the DB
    }
}

