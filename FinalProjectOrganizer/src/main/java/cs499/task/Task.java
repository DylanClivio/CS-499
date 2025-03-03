package cs499.task;

public class Task {
    private String taskId;
    private String taskName;
    private String description;

    // Constructor
    public Task(String taskId, String taskName, String description) {
        if (taskId == null) {
            throw new IllegalArgumentException("Task ID cannot be null.");
        }
        if (taskId.length() > 10) {
            throw new IllegalArgumentException("Task ID cannot exceed 10 characters.");
        }
        if (taskName == null) {
            throw new IllegalArgumentException("Task name cannot be null.");
        }
        if (taskName.length() > 50) {
            throw new IllegalArgumentException("Task name cannot exceed 50 characters.");
        }
        if (description == null) {
            throw new IllegalArgumentException("Task description cannot be null.");
        }
        if (description.length() > 100) {
            throw new IllegalArgumentException("Task description cannot exceed 100 characters.");
        }

        this.taskId = taskId;
        this.taskName = taskName;
        this.description = description;
    }

    // Getter and Setter for Task ID
    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        if (taskId == null) {
            throw new IllegalArgumentException("Task ID cannot be null.");
        }
        if (taskId.length() > 10) {
            throw new IllegalArgumentException("Task ID cannot exceed 10 characters.");
        }
        this.taskId = taskId;
    }

    // Getter and Setter for Task Name
    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        if (taskName == null) {
            throw new IllegalArgumentException("Task name cannot be null.");
        }
        if (taskName.length() > 50) {
            throw new IllegalArgumentException("Task name cannot exceed 50 characters.");
        }
        this.taskName = taskName;
    }

    // Getter and Setter for Task Description
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description == null) {
            throw new IllegalArgumentException("Task description cannot be null.");
        }
        if (description.length() > 100) {
            throw new IllegalArgumentException("Task description cannot exceed 100 characters.");
        }
        this.description = description;
    }
}