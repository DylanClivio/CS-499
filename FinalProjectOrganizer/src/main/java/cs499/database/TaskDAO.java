package cs499.database;

import cs499.task.Task;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskDAO {

    private Connection connection;

    // Constructor
    public TaskDAO(Connection connection) {
        this.connection = connection;
    }

    // Method to create a new task
    public void createTask(Task task) throws SQLException {
        String sql = "INSERT INTO tasks (task_id, name, description) VALUES (?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, task.getTaskId());
            stmt.setString(2, task.getTaskName());
            stmt.setString(3, task.getDescription());
            stmt.executeUpdate();
        }
    }

    // Method to search for tasks (keyword search)
    public List<Task> searchTasks(String searchTerm) throws SQLException {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT task_id, name, description FROM tasks WHERE task_id LIKE ? OR name LIKE ? OR description LIKE ?";

        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            searchTerm = "%"; // Return all tasks if no search term is provided
        } else {
            searchTerm = "%" + searchTerm + "%"; // Wildcard search
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, searchTerm);
            stmt.setString(2, searchTerm);
            stmt.setString(3, searchTerm);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String taskId = rs.getString("task_id");
                    String taskName = rs.getString("name"); // Match database column
                    String description = rs.getString("description");

                    tasks.add(new Task(taskId, taskName, description));
                }
            }
        }
        return tasks;
    }

    // Method to find a task by ID
    public Task findTaskById(String taskId) throws SQLException {
        if (taskId == null || taskId.trim().isEmpty()) {
            return null; // Prevent invalid queries
        }

        String sql = "SELECT task_id, name, description FROM tasks WHERE task_id = ?";

        System.out.println("DEBUG: Executing SQL -> " + sql + " with taskId: " + taskId); // Debugging

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, taskId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String taskName = rs.getString("name"); // Match column name in DB
                    String description = rs.getString("description");

                    System.out.println("DEBUG: Task found -> " + taskId + ", " + taskName + ", " + description);

                    return new Task(taskId, taskName, description);
                }
            }
        }
        System.out.println("DEBUG: No task found for taskId: " + taskId);
        return null; // Return null if no task is found
    }

    // Method to check if a task ID already exists
    public boolean taskExists(String taskId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM tasks WHERE task_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, taskId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }
}