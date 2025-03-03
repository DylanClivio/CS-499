package cs499.servlet;

import cs499.task.Task;
import cs499.database.DatabaseConnection;
import cs499.service.TaskService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

// Servlet to handle task-related CRUD operations via HTTP requests

@WebServlet("/tasks")
public class TaskServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(TaskServlet.class);
    private TaskService taskService;

    @Override
    public void init() throws ServletException {
        try {
            Connection connection = DatabaseConnection.getConnection();
            taskService = new TaskService(connection);
        } catch (SQLException e) {
            throw new ServletException("Failed to initialize TaskService", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            switch (action) {
                case "add":
                    addTask(request, response);
                    break;
                case "update":
                    updateTask(request, response);
                    break;
                case "delete":
                    deleteTask(request, response);
                    break;
                default:
                    sendJsonResponse(response, "{\"error\":\"Invalid action.\"}", HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (SQLException e) {
            logger.error("Error handling task request", e);
            sendJsonResponse(response, "{\"error\":\"An error occurred while processing the task request.\"}", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void addTask(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        String taskId = request.getParameter("taskId");
        String taskName = request.getParameter("taskName");
        String description = request.getParameter("description");

        boolean success = taskService.addTask(taskId, taskName, description);
        sendJsonResponse(response, success ? "{\"message\":\"Task added successfully.\"}" : "{\"error\":\"Failed to add task.\"}",
                success ? HttpServletResponse.SC_OK : HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    private void updateTask(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        String taskId = request.getParameter("taskId");
        String newTaskName = request.getParameter("taskName");
        String newDescription = request.getParameter("description");

        boolean success = taskService.updateTaskName(taskId, newTaskName) && taskService.updateTaskDescription(taskId, newDescription);
        sendJsonResponse(response, success ? "{\"message\":\"Task updated successfully.\"}" : "{\"error\":\"Failed to update task.\"}",
                success ? HttpServletResponse.SC_OK : HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    private void deleteTask(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        String taskId = request.getParameter("taskId");

        boolean success = taskService.deleteTask(taskId);
        sendJsonResponse(response, success ? "{\"message\":\"Task deleted successfully.\"}" : "{\"error\":\"Failed to delete task.\"}",
                success ? HttpServletResponse.SC_OK : HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String taskId = request.getParameter("taskId");

        try {
            Optional<Task> task = taskService.getTask(taskId);
            if (task.isPresent()) {
                sendJsonResponse(response, "{\"message\":\"Task found: " + task.get().getTaskName() + "\"}", HttpServletResponse.SC_OK);
            } else {
                sendJsonResponse(response, "{\"error\":\"Task not found.\"}", HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("Error retrieving task", e);
            sendJsonResponse(response, "{\"error\":\"An error occurred while retrieving the task.\"}", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void sendJsonResponse(HttpServletResponse response, String jsonResponse, int statusCode) throws IOException {
        response.setContentType("application/json");
        response.setStatus(statusCode);
        try (PrintWriter out = response.getWriter()) {
            out.println(jsonResponse);
        }
    }
}