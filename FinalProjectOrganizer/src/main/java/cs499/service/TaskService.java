package cs499.service;

import java.sql.*;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cs499.task.Task;

// The TaskService class provides functionality to add, delete, update, and retrieve tasks from a MySQL database.
public class TaskService {
    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);
    private final Connection connection;

    public TaskService(Connection connection) {
        this.connection = connection;
    }

    // Adds a new task to the database.
    public boolean addTask(String taskId, String name, String description) {
        if (taskExists(taskId)) {
            logger.error("Task with ID {} already exists.", taskId);
            return false;
        }

        String sql = "INSERT INTO tasks (taskId, name, description) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, taskId);
            stmt.setString(2, name);
            stmt.setString(3, description);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logDatabaseError("adding task", taskId, e);
            return false;
        }
    }

    // Deletes a task by ID from the database.
    public boolean deleteTask(String taskId) {
        String sql = "DELETE FROM tasks WHERE taskId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, taskId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logDatabaseError("deleting task", taskId, e);
            return false;
        }
    }

    // Updates the name of an existing task.
    public boolean updateTaskName(String taskId, String newName) {
        return updateTaskField(taskId, "name", newName);
    }

    // Updates the description of an existing task.
    public boolean updateTaskDescription(String taskId, String newDescription) {
        return updateTaskField(taskId, "description", newDescription);
    }

    // Helper method to update a task field
    private boolean updateTaskField(String taskId, String field, String newValue) {
        if (!taskExists(taskId)) {
            logger.error("Task with ID {} does not exist.", taskId);
            return false;
        }

        String sql = "UPDATE tasks SET " + field + " = ? WHERE taskId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newValue);
            stmt.setString(2, taskId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logDatabaseError("updating task " + field, taskId, e);
            return false;
        }
    }

    // Retrieves a task by its unique ID.
    public Optional<Task> getTask(String taskId) {
        String sql = "SELECT * FROM tasks WHERE taskId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, taskId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Task(
                        rs.getString("taskId"),
                        rs.getString("name"),
                        rs.getString("description")
                    ));
                }
            }
        } catch (SQLException e) {
            logDatabaseError("retrieving task", taskId, e);
        }
        return Optional.empty();
    }

    // Helper method to check if a task already exists by taskId
    private boolean taskExists(String taskId) {
        String sql = "SELECT COUNT(*) FROM tasks WHERE taskId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, taskId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            logDatabaseError("checking if task exists", taskId, e);
        }
        return false;
    }

    // Helper method for logging database-related errors consistently
    private void logDatabaseError(String operation, String taskId, SQLException e) {
        logger.error("Error while {} with task ID {}: {}", operation, taskId, e.getMessage(), e);
    }
}