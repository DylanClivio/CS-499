package cs499.servlet;

import cs499.task.Task;
import cs499.database.TaskDAO;
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

// Servlet for handling task search requests, returning JSON responses

@WebServlet("/SearchTaskServlet")
public class SearchTaskServlet extends HttpServlet {
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String taskId = request.getParameter("taskId"); // Check if searching by ID
        String searchTerm = request.getParameter("query"); // General search

        try (Connection connection = DatabaseConnection.getConnection()) {
            TaskDAO taskDAO = new TaskDAO(connection);

            if (taskId != null && !taskId.isEmpty()) {
                // Search by specific task ID
                Task task = taskDAO.findTaskById(taskId);
                String json = (task != null) ? new Gson().toJson(task) : "{}";
                response.getWriter().write(json);
            } else {
                // Perform general search
                List<Task> tasks = taskDAO.searchTasks(searchTerm != null ? searchTerm : "");
                String json = new Gson().toJson(tasks);
                response.getWriter().write(json);
            }
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Database error: " + e.getMessage() + "\"}");
        }
    }
}