package cs499.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataSourceConfig {

    private static final String URL = "jdbc:mysql://localhost:3306/cs499";
    private static final String USERNAME = System.getenv("DB_USERNAME");
    private static final String PASSWORD = System.getenv("DB_PASSWORD");

    static {
        if (USERNAME == null || PASSWORD == null) {
            throw new IllegalStateException("Database username or password not found in environment variables.");
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to load MySQL JDBC driver", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}