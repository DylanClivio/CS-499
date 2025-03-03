package cs499.servlet;

import cs499.appointment.Appointment;
import cs499.database.AppointmentDAO;
import cs499.database.DatabaseConnection;
import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/SearchAppointmentServlet")
public class SearchAppointmentServlet extends HttpServlet {
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Check searching by ID
        String appointmentId = request.getParameter("appointmentId"); 
        String searchTerm = request.getParameter("query"); // General search

        try (Connection connection = DatabaseConnection.getConnection()) {
            AppointmentDAO appointmentDAO = new AppointmentDAO(connection);

            if (appointmentId != null && !appointmentId.isEmpty()) {
                // Search by specific appointment ID
                Appointment appointment = appointmentDAO.findAppointmentById(appointmentId);
                String json = (appointment != null) ? new Gson().toJson(appointment) : "{}";
                response.getWriter().write(json);
            } else {
                // Perform general search
                List<Appointment> appointments = appointmentDAO.searchAppointments(searchTerm != null ? searchTerm : "");
                String json = new Gson().toJson(appointments);
                response.getWriter().write(json);
            }
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Database error: " + e.getMessage() + "\"}");
        }
    }
}