package cs499.service;

import cs499.appointment.Appointment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppointmentService {
    private final Connection connection;
    private static final Logger logger = LoggerFactory.getLogger(AppointmentService.class);

    public AppointmentService(Connection connection) {
        this.connection = connection;
    }

    // Add an appointment to the database
    public boolean addAppointment(Appointment appointment) {
        if (appointment.getAppointmentId() == null || appointment.getAppointmentId().isEmpty()) {
            throw new IllegalArgumentException("Appointment ID cannot be null or empty.");
        }

        String sql = "INSERT INTO appointments (appointment_id, appointment_date, description) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, appointment.getAppointmentId());
            stmt.setDate(2, appointment.getAppointmentDate());
            stmt.setString(3, appointment.getDescription());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error adding appointment: {}", appointment.getAppointmentId(), e);
            return false;
        }
    }

    // Get an appointment by ID
    public Appointment getAppointment(String appointmentId) {
        String sql = "SELECT * FROM appointments WHERE appointment_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, appointmentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Appointment(
                        rs.getString("appointment_id"),
                        rs.getDate("appointment_date"),
                        rs.getString("description")
                    );
                }
            }
        } catch (SQLException e) {
            logger.error("Error retrieving appointment: {}", appointmentId, e);
        }
        return null;
    }

    // Update an appointment
    public boolean updateAppointment(Appointment appointment) {
        String sql = "UPDATE appointments SET appointment_date = ?, description = ? WHERE appointment_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, appointment.getAppointmentDate());
            stmt.setString(2, appointment.getDescription());
            stmt.setString(3, appointment.getAppointmentId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error updating appointment: {}", appointment.getAppointmentId(), e);
            return false;
        }
    }

    // Delete an appointment
    public boolean deleteAppointment(String appointmentId) {
        String sql = "DELETE FROM appointments WHERE appointment_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, appointmentId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error deleting appointment: {}", appointmentId, e);
            return false;
        }
    }

    // Get all appointments from the database
    public List<Appointment> getAllAppointments() {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM appointments";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                appointments.add(new Appointment(
                    rs.getString("appointment_id"),
                    rs.getDate("appointment_date"),
                    rs.getString("description")
                ));
            }
        } catch (SQLException e) {
            logger.error("Error retrieving all appointments", e);
        }
        return appointments;
    }
}