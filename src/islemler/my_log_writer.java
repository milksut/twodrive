package islemler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class my_log_writer
{
    private static final String working_directory = System.getProperty("user.dir") + "\\files\\";
    private static final Logger logger = Logger.getLogger(my_log_writer.class.getName());

    public my_log_writer(String Logging_file) {
        try {

            // Create a file handler for logging to a file
            FileHandler fileHandler = new FileHandler(working_directory + Logging_file, true);
            fileHandler.setLevel(Level.ALL);

            // Set up the formatter for log messages
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);

            // Add handlers to the logger
            logger.addHandler(fileHandler);
            logger.setLevel(Level.ALL); // You can adjust the log level here (e.g., Level.INFO)
        }
        catch (IOException e) {
            System.err.println("Error setting up file handler for logging: " + e.getMessage());
        }
    }

    // Method to log an info message
    public void logInfo(String message) {
        logger.info(message + " Tarih: " + LocalDateTime.now() + "\n");
    }

    // Method to log a warning message
    public void logWarning(String message) {
        logger.warning(message + " Tarih: " + LocalDateTime.now() + "\n");
    }

    // Method to log a severe error message
    public void logError(String message) {
        logger.severe(message + " Tarih: " + LocalDateTime.now() + "\n");
    }

    // Method to log a specific exception stack trace
    public void logError(Exception e) {
        logger.log(Level.SEVERE, e.getMessage() + " Tarih: " + LocalDateTime.now() + "\n", e);
    }

    // Method to log debug level messages (optional)
    public void logDebug(String message) {
        logger.fine(message + " Tarih: " + LocalDateTime.now() + "\n");
    }

}
