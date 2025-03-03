package cs499.database;

import cs499.appointment.Appointment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {

    private Connection connection;

    // Constructor
    public AppointmentDAO(Connection connection) {
        this.connection = connection;
    }

    // Method to create a new appointment
    public void createAppointment(Appointment appointment) throws SQLException {
        String sql = "INSERT INTO appointments (appointment_id, appointment_date, description) VALUES (?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, appointment.getAppointmentId());
            stmt.setTimestamp(2, new Timestamp(appointment.getAppointmentDate().getTime())); // Ensure correct format
            stmt.setString(3, appointment.getDescription());
            stmt.executeUpdate();
        }
    }

    // Method to search for appointments with filtering capability
    public List<Appointment> searchAppointments(String searchTerm) throws SQLException {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT appointment_id, appointment_date, description FROM appointments WHERE " +
                     "appointment_id LIKE ? OR description LIKE ?";

        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            searchTerm = "%"; // Return all appointments if no search term is provided
        } else {
            searchTerm = "%" + searchTerm + "%"; // Wildcard search
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, searchTerm);
            stmt.setString(2, searchTerm);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String appointmentId = rs.getString("appointment_id");
                    Timestamp timestamp = rs.getTimestamp("appointment_date");

                    // Default to the current date if NULL
                    Date appointmentDate = (timestamp != null) ? new Date(timestamp.getTime()) : new Date(System.currentTimeMillis());

                    String description = rs.getString("description");

                    appointments.add(new Appointment(appointmentId, appointmentDate, description));
                }
            }
        }
        return appointments;
    }

    // Method to find an appointment by ID
    public Appointment findAppointmentById(String appointmentId) throws SQLException {
        String sql = "SELECT appointment_id, appointment_date, description FROM appointments WHERE appointment_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, appointmentId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Timestamp timestamp = rs.getTimestamp("appointment_date");
                    Date appointmentDate = (timestamp != null) ? new Date(timestamp.getTime()) : new Date(System.currentTimeMillis());
                    String description = rs.getString("description");

                    return new Appointment(appointmentId, appointmentDate, description);
                }
            }
        }
        return null; // Return null if no appointment is found
    }

    // Method to check if an appointment ID already exists
    public boolean appointmentExists(String appointmentId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM appointments WHERE appointment_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, appointmentId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }
}