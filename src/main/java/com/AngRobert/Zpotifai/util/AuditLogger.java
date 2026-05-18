package com.AngRobert.Zpotifai.util;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.logging.*;

public class AuditLogger {
    private static final Logger logger = Logger.getLogger(AuditLogger.class.getName());

    static {
        try {
            // Ensure logs directory exists
            File logDir = new File("logs");
            if (!logDir.exists()) {
                logDir.mkdir();
            }

            // Create a FileHandler that appends to the CSV
            FileHandler fileHandler = new FileHandler("logs/audit_log.csv", true);
            
            // Custom Formatter to strictly follow: Action,Timestamp
            fileHandler.setFormatter(new Formatter() {
                @Override
                public String format(LogRecord record) {
                    // Action name comes from record.getMessage()
                    // Timestamp uses ISO-8601 (e.g. 2026-05-18T14:30:05.123)
                    String action = record.getMessage();
                    String timestamp = LocalDateTime.now().toString();
                    return action + "," + timestamp + "\n";
                }
            });

            logger.addHandler(fileHandler);
            
            // Disable logging to the console so it stays silent in the CLI
            logger.setUseParentHandlers(false);
            
        } catch (IOException e) {
            System.err.println("Could not initialize audit logger: " + e.getMessage());
        }
    }

    public static void log(String action) {
        logger.info(action);
    }
}
