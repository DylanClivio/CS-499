package cs499.servlet;

import cs499.task.Task;
import cs499.database.TaskDAO;
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

@WebServlet("/CreateTaskServlet")  // Maps to "/CreateTaskServlet" URL
public class CreateTaskServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String taskId = request.getParameter("taskId");
        String name = request.getParameter("name");
        String description = request.getParameter("description");

        // Validate Task ID (Max length: 10)
        if (taskId == null || taskId.trim().isEmpty() || taskId.length() > 10) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Task ID (max length 10 characters).");
            return;
        }

        // Validate Task Name (Max length: 50)
        if (name == null || name.trim().isEmpty() || name.length() > 50) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Task Name (max length 50 characters).");
            return;
        }

        // Validate Description (Max length: 100)
        if (description == null || description.trim().isEmpty() || description.length() > 100) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Task Description (max length 100 characters).");
            return;
        }

        // Create a new Task object
        Task task = new Task(taskId, name, description);

        // Insert the task into the database using TaskDAO
        try (Connection connection = DatabaseConnection.getConnection()) {
            TaskDAO taskDAO = new TaskDAO(connection);

            // Check if task ID already exists before inserting
            if (taskDAO.taskExists(taskId)) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Task ID already exists.");
                return;
            }

            taskDAO.createTask(task);

            // Forward to task-success.html after successful creation
            RequestDispatcher dispatcher = request.getRequestDispatcher("task-success.html");
            dispatcher.forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error: " + e.getMessage());
        }
    }
}