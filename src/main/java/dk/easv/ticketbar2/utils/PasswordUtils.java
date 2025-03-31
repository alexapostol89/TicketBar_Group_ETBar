package dk.easv.ticketbar2.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtils {
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Method to hash password
    public static String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }

    // Method to check if password matches
    public static boolean checkPassword(String rawPassword, String hashedPassword) {
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }
}
