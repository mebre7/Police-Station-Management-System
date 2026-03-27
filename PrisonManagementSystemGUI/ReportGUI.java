package PrisonManagementSystemGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReportGUI {

    private static final String URL = "jdbc:mysql://localhost:3306/prison_management_system";
    private static final String USER = "root";
    private static final String PASSWORD = "24mebredbms36";

    public JPanel createPanel() {
        // Create the main panel
        JPanel panel = new JPanel(new GridLayout(0, 1));

        // Buttons for the GUI
        JButton viewOperationReportBtn = new JButton("View Operation Report");
        JButton viewIncidentReportBtn = new JButton("View Incident Report");

        panel.add(viewOperationReportBtn);
        panel.add(viewIncidentReportBtn);

        // Add functionality to buttons
        viewOperationReportBtn.addActionListener((ActionEvent e) -> {
            String userRole = JOptionPane.showInputDialog("Enter your role (Warden/Staff):");
            if (userRole.equalsIgnoreCase("Warden")) {
                viewOperationReport();
            } else {
                JOptionPane.showMessageDialog(null, "Access Denied to Operation Report.");
            }
        });

        viewIncidentReportBtn.addActionListener((ActionEvent e) -> {
            String userRole = JOptionPane.showInputDialog("Enter your role (Warden/Staff):");
            if (userRole.equalsIgnoreCase("Staff")) {
                viewIncidentReport();
            } else {
                JOptionPane.showMessageDialog(null, "Access Denied to Incident Report.");
            }
        });

        return panel;
    }

    // Method to generate operation report
    private void viewOperationReport() {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "SELECT * FROM inmates"; // Fetch all inmates for the operation report
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            StringBuilder report = new StringBuilder();
            report.append("Monthly Operations Report:\n\n");
            while (resultSet.next()) {
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String crimeCommitted = resultSet.getString("crimeCommitted");
                String cellNumber = resultSet.getString("cellNumber");
                report.append(String.format("Inmate: %s %s - Crime: %s - Cell: %s\n", firstName, lastName, crimeCommitted, cellNumber));
            }

            JOptionPane.showMessageDialog(null, report.toString());

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching data for operation report: " + e.getMessage());
        }
    }

    // Method to generate incident report
    private void viewIncidentReport() {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "SELECT * FROM inmates WHERE crimeCommitted LIKE '%Fight%' OR crimeCommitted LIKE '%Unauthorized%'"; // Fetch incidents
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            List<String> incidents = new ArrayList<>();
            while (resultSet.next()) {
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String crimeCommitted = resultSet.getString("crimeCommitted");
                incidents.add(String.format("Inmate: %s %s - Crime: %s", firstName, lastName, crimeCommitted));
            }

            if (incidents.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No incidents to report.");
            } else {
                StringBuilder incidentReport = new StringBuilder("Security Incident Report:\n\n");
                for (String incident : incidents) {
                    incidentReport.append(incident).append("\n");
                }
                JOptionPane.showMessageDialog(null, incidentReport.toString());
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching data for incident report: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // Create an instance of ReportGUI and display the panel
        ReportGUI reportGUI = new ReportGUI();
        JPanel panel = reportGUI.createPanel();

        JFrame frame = new JFrame("Report Management");
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.setVisible(true);
    }
}
