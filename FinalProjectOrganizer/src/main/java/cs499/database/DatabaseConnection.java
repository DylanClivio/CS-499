package cs499.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/cs499"; // Update DB name
    private static final String USER = "root"; // Update with correct username
    private static final String PASSWORD = "VarunBitcoin82399$"; // Update with correct password

    static {
        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL JDBC Driver loaded successfully.");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to load MySQL JDBC driver", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Error getting connection: " + e.getMessage());
            throw e;
        }
    }

    public static void testConnection() {
        try (Connection connection = getConnection()) {
            if (connection != null) {
                System.out.println("Successfully connected to the database.");
            } else {
                System.err.println("Failed to connect to the database.");
            }
        } catch (SQLException e) {
            System.err.println("Database connection test failed: " + e.getMessage());
        }
    }
}