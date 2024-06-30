import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskList {
    private Connection connection;

    public TaskList(String jdbcURL, String user, String password) throws SQLException {
        connection = DriverManager.getConnection(jdbcURL, user, password);
    }

    public void addTask(String name) throws SQLException {
        String sql = "INSERT INTO tasks (name, completed) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setBoolean(2, false);
            statement.executeUpdate();
        }
    }

    public void updateTask(int id, boolean completed) throws SQLException {
        String sql = "UPDATE tasks SET completed = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBoolean(1, completed);
            statement.setInt(2, id);
            statement.executeUpdate();
        }
    }

    public void deleteTask(int id) throws SQLException {
        String sql = "DELETE FROM tasks WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    public List<Task> getAllTasks() throws SQLException {
        List<Task> taskList = new ArrayList<>();
        String sql = "SELECT * FROM tasks";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                boolean completed = resultSet.getBoolean("completed");
                Task task = new Task(id, name, completed);
                taskList.add(task);
            }
        }
        return taskList;
    }
}