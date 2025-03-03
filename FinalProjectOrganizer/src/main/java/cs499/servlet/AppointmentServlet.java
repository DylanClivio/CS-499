package cs499.servlet;

import cs499.appointment.Appointment;
import cs499.service.AppointmentService;
import cs499.database.DatabaseConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

@WebServlet("/appointments") // This maps the servlet to "/appointments"
public class AppointmentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private AppointmentService appointmentService;

    @Override
    public void init() throws ServletException {
        try {
            System.out.println("Initializing AppointmentServlet...");
            Connection connection = DatabaseConnection.getConnection();
            this.appointmentService = new AppointmentService(connection);
            System.out.println("✅ AppointmentServlet initialized successfully.");
        } catch (SQLException e) {
            System.err.println("❌ ERROR: Database connection initialization failed - " + e.getMessage());
            e.printStackTrace();
            throw new ServletException("Database connection initialization failed", e);
        }
    }

    private void sendJsonResponse(HttpServletResponse response, String jsonResponse, int statusCode) throws IOException {
        response.setContentType("application/json");
        response.setStatus(statusCode);
        try (PrintWriter out = response.getWriter()) {
            out.println(jsonResponse);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("🔍 Processing GET request...");

        String appointmentId = request.getParameter("id");
        if (appointmentId == null) {
            sendJsonResponse(response, "{\"error\": \"Missing appointment ID\"}", HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            Appointment appointment = appointmentService.getAppointment(appointmentId);
            if (appointment != null) {
                // Format the appointment date before returning it
                String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(appointment.getAppointmentDate());

                String jsonResponse = String.format(
                    "{\"appointmentId\":\"%s\", \"appointmentDate\":\"%s\", \"description\":\"%s\"}",
                    appointment.getAppointmentId(),
                    formattedDate,
                    appointment.getDescription()
                );
                sendJsonResponse(response, jsonResponse, HttpServletResponse.SC_OK);
            } else {
                sendJsonResponse(response, "{\"error\": \"Appointment not found\"}", HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();
            sendJsonResponse(response, "{\"error\": \"An error occurred while processing the request\"}", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}