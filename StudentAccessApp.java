package SunLab;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class StudentAccessApp {
    private static final String DB_URL = "jdbc:mysql://localhost/student_access";
    private static final String USER = "root"; // Default user for XAMPP
    private static final String PASSWORD = ""; // Default password for XAMPP

    public static void main(String[] args) {
        // Ensure the Swing GUI is created on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Student Access Log");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 400);

            JPanel panel = new JPanel();
            frame.add(panel);
            placeComponents(panel);

            frame.setVisible(true);
        });
    }

    private static void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel userLabel = new JLabel("User ID:");
        userLabel.setBounds(10, 20, 80, 25);
        panel.add(userLabel);

        JTextField userText = new JTextField(20);
        userText.setBounds(100, 20, 165, 25);
        panel.add(userText);

        JLabel userTypeLabel = new JLabel("User Type:");
        userTypeLabel.setBounds(10, 60, 80, 25);
        panel.add(userTypeLabel);

        String[] userTypes = {"student", "admin"};
        JComboBox<String> userTypeCombo = new JComboBox<>(userTypes);
        userTypeCombo.setBounds(100, 60, 165, 25);
        panel.add(userTypeCombo);

        JButton logButton = new JButton("Log Access");
        logButton.setBounds(10, 100, 150, 25);
        panel.add(logButton);

        JButton activateButton = new JButton("Activate User");
        activateButton.setBounds(10, 140, 150, 25);
        panel.add(activateButton);

        JButton suspendButton = new JButton("Suspend User");
        suspendButton.setBounds(10, 180, 150, 25);
        panel.add(suspendButton);

        JButton reactivateButton = new JButton("Reactivate User");
        reactivateButton.setBounds(10, 220, 150, 25);
        panel.add(reactivateButton);

        logButton.addActionListener(e -> {
            String userId = userText.getText();
            logAccess(userId);
        });

        activateButton.addActionListener(e -> {
            String userId = userText.getText();
            changeUserStatus(userId, "active");
        });

        suspendButton.addActionListener(e -> {
            String userId = userText.getText();
            changeUserStatus(userId, "suspended");
        });

        reactivateButton.addActionListener(e -> {
            String userId = userText.getText();
            changeUserStatus(userId, "reactivated");
        });
    }

    private static void logAccess(String userId) {
        String query = "INSERT INTO access_logs (user_id) VALUES (?)";

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, Integer.parseInt(userId));
            preparedStatement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Access logged successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error logging access.");
        }
    }

    private static void changeUserStatus(String userId, String status) {
        String query = "UPDATE users SET status = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, status);
            preparedStatement.setInt(2, Integer.parseInt(userId));
            preparedStatement.executeUpdate();
            JOptionPane.showMessageDialog(null, "User status updated to " + status + ".");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error updating user status.");
        }
    }
}
