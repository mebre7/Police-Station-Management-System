package PrisonManagementSystemGUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ScheduleGUI {

    JTable personalScheduleTable;
    JTable wardenScheduleTable;

    JTabbedPane tabbedPane;

    // Column names for the JTable
    String[] columnNames = {"Day", "8 AM - 10 AM", "10 AM - 12 PM", "12 PM - 2 PM", "2 PM - 4 PM"};

    // Default schedule template for staff members
    String[][] staffTemplate = {
            {"Monday", "", "", "", ""},
            {"Tuesday", "", "", "", ""},
            {"Wednesday", "", "", "", ""},
            {"Thursday", "", "", "", ""},
            {"Friday", "", "", "", ""}
    };

    // Warden schedule
    String[][] wardenSchedule = {
            {"Monday", "Meeting with Officials", "Prison Tour", "Lunch", "Paperwork"},
            {"Tuesday", "Security Briefing", "Review Inmate Files", "Lunch", "Meeting with Staff"},
            {"Wednesday", "Court Visit", "Inspect Security", "Lunch", "Meeting with Contractors"},
            {"Thursday", "Budget Review", "Inmate Review", "Lunch", "Security Overview"},
            {"Friday", "Inspect Facilities", "Meeting with Security", "Lunch", "Administrative Work"}
    };

    // Dynamic storage for staff members' schedules
    Map<String, String[][]> staffSchedules = new HashMap<>();

    DefaultTableModel personalModel;
    DefaultTableModel wardenModel;

    // Constructor
    public ScheduleGUI() {
        // Initialize schedules for multiple staff members
        initializeStaffSchedules();

        // Initialize models for the tables
        personalModel = new DefaultTableModel(staffSchedules.get("Staff"), columnNames); // Staff personal schedule
        wardenModel = new DefaultTableModel(wardenSchedule, columnNames); // Warden's own schedule

        // Create the tables
        personalScheduleTable = new JTable(personalModel);
        wardenScheduleTable = new JTable(wardenModel);

        // Create the tabbed pane for switching between schedules
        tabbedPane = new JTabbedPane();

        // Add the "Personal Schedule" tab
        tabbedPane.addTab("Personal Schedule", createPersonalScheduleTab());

        // Add the "Warden's Schedule" tab
        tabbedPane.addTab("Warden's Schedule", createWardenScheduleTab());
    }

    // Initialize default schedules for staff members
    private void initializeStaffSchedules() {
        // Create default schedules for each staff member
        staffSchedules.put("Staff", new String[][]{
                {"Monday", "Morning Roll Call", "Guard Duty", "Lunch", "Surveillance Duty"},
                {"Tuesday", "Inmate Supervision", "Medical Checkups", "Lunch", "Patrol Duty"},
                {"Wednesday", "Guard Duty", "Paperwork", "Lunch", "Inmate Transfer"},
                {"Thursday", "Facility Maintenance", "Inmate Supervision", "Lunch", "Patrol Duty"},
                {"Friday", "Morning Roll Call", "Training", "Lunch", "Guard Duty"}
        });
    }

    // Create a tab for displaying the logged-in user's personal schedule
    private JPanel createPersonalScheduleTab() {
        JPanel panel = new JPanel(new BorderLayout());

        // Add the personal schedule table for staff
        JScrollPane scrollPane = new JScrollPane(personalScheduleTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // Create a tab for displaying the Warden's own schedule
    private JPanel createWardenScheduleTab() {
        JPanel panel = new JPanel(new BorderLayout());

        // Add the warden schedule table
        JScrollPane scrollPane = new JScrollPane(wardenScheduleTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // Method to create the panel for this class
    public JPanel createPanel() {
        // Create a new panel and add the tabbed pane
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(tabbedPane, BorderLayout.CENTER);
        return panel;
    }

    // Method to get the main panel for embedding (alternative name for `createPanel()`)
    public JPanel getMainPanel() {
        return createPanel();
    }

    public static void main(String[] args) {
        // Create the main frame
        SwingUtilities.invokeLater(() -> {
            JFrame mainFrame = new JFrame("Main Application");
            mainFrame.setSize(700, 500);
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Add ScheduleGUI panel to the main application
            ScheduleGUI scheduleGUI = new ScheduleGUI();
            JPanel panel = scheduleGUI.createPanel(); // Using createPanel()
            mainFrame.add(panel);

            mainFrame.setVisible(true);
        });
    }
}
