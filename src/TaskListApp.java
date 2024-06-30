import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class TaskListApp {
    private TaskList taskList;
    private DefaultListModel<Task> taskListModel;

    public TaskListApp() {
        try {
            taskList = new TaskList("jdbc:mysql://localhost:3306/tarefasdb", "root", "220802");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao se conectar com o Banco de Dados", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        JFrame frame = new JFrame("App de Lista de Tarefas");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);

        taskListModel = new DefaultListModel<>();
        JList<Task> taskJList = new JList<>(taskListModel);
        taskJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(taskJList);

        JTextField taskField = new JTextField("Adicione uma Tarefa...");
        JButton addButton = new JButton("Adicionar Tarefa");
        JButton completeButton = new JButton("Completar Tarefa");
        JButton deleteButton = new JButton("Deletar Tarefa");

        addButton.addActionListener(e -> {
            String taskName = taskField.getText();
            if (!taskName.isEmpty()) {
                try {
                    taskList.addTask(taskName);
                    refreshTaskList();
                    taskField.setText("");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        completeButton.addActionListener(e -> {
            Task selectedTask = taskJList.getSelectedValue();
            if (selectedTask != null) {
                try {
                    taskList.updateTask(selectedTask.getId(), true);
                    refreshTaskList();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        deleteButton.addActionListener(e -> {
            Task selectedTask = taskJList.getSelectedValue();
            if (selectedTask != null) {
                try {
                    taskList.deleteTask(selectedTask.getId());
                    refreshTaskList();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(taskField, BorderLayout.CENTER);
        panel.add(addButton, BorderLayout.EAST);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        buttonPanel.add(completeButton);
        buttonPanel.add(deleteButton);

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(panel, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        refreshTaskList();

        frame.setVisible(true);
    }

    private void refreshTaskList() {
        try {
            List<Task> tasks = taskList.getAllTasks();
            taskListModel.clear();
            for (Task task : tasks) {
                taskListModel.addElement(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TaskListApp::new);
    }
}