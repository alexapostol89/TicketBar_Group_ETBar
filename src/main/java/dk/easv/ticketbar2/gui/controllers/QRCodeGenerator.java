package dk.easv.ticketbar2.gui.controllers;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class QRCodeGenerator {

    public static String generateQRCode(String data, String fileName) throws WriterException, IOException {
        // Define the directory and file path
        String directoryPath = "dk/easv/ticketbar2/pictures/QRCode";
        File directory = new File(directoryPath);

        // Ensure the directory exists
        if (!directory.exists()) {
            directory.mkdirs(); // Create the directory if it does not exist
        }

        String filePath = directoryPath + "QRCode" + fileName;

        BitMatrix bitMatrix = new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, 300, 300);
        MatrixToImageWriter.writeToFile(bitMatrix, "png", Paths.get(filePath).toFile());

        return filePath; // Return file path so it can be stored in the DB
    }

    public static String generateBarcode(String data, String fileName) throws WriterException, IOException {
        // Define the directory and file path
        String directoryPath = "dk/easv/ticketbar2/pictures/barcodes/";
        File directory = new File(directoryPath);

        // Ensure the directory exists
        if (!directory.exists()) {
            directory.mkdirs(); // Create the directory if it does not exist
        }

        String filePath = directoryPath + "Barcode" + fileName;

        BitMatrix bitMatrix = new MultiFormatWriter().encode(data, BarcodeFormat.CODE_128, 300, 100);
        MatrixToImageWriter.writeToFile(bitMatrix, "png", Paths.get(filePath).toFile());

        return filePath; // Return file path so it can be stored in the DB
    }
}
