package cs499.appointment;

import java.sql.Date;  // Ensure you're importing java.sql.Date

// The Appointment class contains an appointment with an ID, date,
// and a brief description that are all held to constraints.
public class Appointment {
    private final String appointmentId;
    private final Date appointmentDate;
    private final String description;

    // Constructor for creating an Appointment object from database data
    public Appointment(String appointmentId, Date appointmentDate, String description) {
        // Validate appointmentId: must not be null or too long
        if (appointmentId == null || appointmentId.length() > 10) {
            throw new IllegalArgumentException("Invalid appointment ID");
        }

        // Allow null or past dates by setting a default value
        this.appointmentDate = (appointmentDate != null) ? appointmentDate : new Date(System.currentTimeMillis());

        // Validate description: must not be null or too long
        if (description == null || description.length() > 50) {
            throw new IllegalArgumentException("Invalid description");
        }

        // Assign values to the fields
        this.appointmentId = appointmentId;
        this.description = description;
    }

    // Getters for the Appointment class fields
    public String getAppointmentId() {
        return appointmentId;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public String getDescription() {
        return description;
    }
}