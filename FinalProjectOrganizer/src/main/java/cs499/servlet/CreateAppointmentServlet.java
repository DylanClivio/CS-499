package cs499.servlet;

import cs499.appointment.Appointment;
import cs499.database.AppointmentDAO;
import cs499.database.DatabaseConnection;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/CreateAppointmentServlet")  // Maps to "/CreateAppointmentServlet" URL
public class CreateAppointmentServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String appointmentId = request.getParameter("appointmentId");
        String appointmentDateStr = request.getParameter("appointmentDate");
        String description = request.getParameter("description");

        // Validate appointmentId (Max length: 10)
        if (appointmentId == null || appointmentId.trim().isEmpty() || appointmentId.length() > 10) {
            request.setAttribute("error", "Invalid Appointment ID (max length 10 characters). Given: " + appointmentId);
            RequestDispatcher dispatcher = request.getRequestDispatcher("index.html");
            dispatcher.forward(request, response);
            return;
        }

        // Validate description (Ensure it's not empty)
        if (description == null || description.trim().isEmpty()) {
            request.setAttribute("error", "Description is required.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("index.html");
            dispatcher.forward(request, response);
            return;
        }

        // Validate appointmentDate format
        java.sql.Date appointmentDate;
        try {
            appointmentDate = java.sql.Date.valueOf(appointmentDateStr);
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", "Invalid date format. Given: " + appointmentDateStr);
            RequestDispatcher dispatcher = request.getRequestDispatcher("index.html");
            dispatcher.forward(request, response);
            return;
        }

        // Create a new appointment object
        Appointment appointment = new Appointment(appointmentId, appointmentDate, description);

        // Get database connection
        try (Connection connection = DatabaseConnection.getConnection()) {
            AppointmentDAO appointmentDAO = new AppointmentDAO(connection);

            // Check if appointment ID already exists
            if (appointmentDAO.appointmentExists(appointmentId)) {
                request.setAttribute("error", "Appointment ID already exists.");
                RequestDispatcher dispatcher = request.getRequestDispatcher("index.html");
                dispatcher.forward(request, response);
                return;
            }

            appointmentDAO.createAppointment(appointment);

            // Forward the request to appointment-success.html
            RequestDispatcher dispatcher = request.getRequestDispatcher("appointment-success.html");
            dispatcher.forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("error", "Database error: " + e.getMessage());
            RequestDispatcher dispatcher = request.getRequestDispatcher("index.html");
            dispatcher.forward(request, response);
        }
    }
}