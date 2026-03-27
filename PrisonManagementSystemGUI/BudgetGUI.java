package PrisonManagementSystemGUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class BudgetGUI implements ActionListener {

    JTable budgetTable;
    JButton addButton;
    JButton editButton;
    JButton deleteButton;

    String[] columnNames = {"Expense Category", "Amount", "Date", "Notes"};
    DefaultTableModel tableModel;

    public BudgetGUI() {
        // No JFrame or setVisible here to prevent premature display
    }

    public JPanel createPanel() {
        // Create the main panel
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Initialize the table with updated budget data
        String[][] updatedData = getUpdatedBudgetData();
        tableModel = new DefaultTableModel(updatedData, columnNames);
        budgetTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(budgetTable);

        // Footer buttons
        addButton = new JButton("Add Expense");
        editButton = new JButton("Edit Expense");
        deleteButton = new JButton("Delete Expense");

        // Add action listeners
        addButton.addActionListener(this);
        editButton.addActionListener(this);
        deleteButton.addActionListener(this);

        // Layout for footer buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        // Add components to the panel
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private String[][] getUpdatedBudgetData() {
        String staffSalaries = getStaffSalariesFromDatabase();
        String[][] updatedData = {
                {"Staff Salaries", staffSalaries, "2024-01-01", "Monthly salaries for prison staff"},
                {"Maintenance", "50000", "2024-01-05", "Repairs for infrastructure"},
                {"Medical Supplies", "15000", "2024-01-07", "Purchase of medical kits"},
                {"Security Equipment", "20000", "2024-01-10", "Upgrade of surveillance system"},
                {"Food & Supplies", "30000", "2024-01-12", "Monthly food and supplies budget"}
        };
        return updatedData;
    }

    private String getStaffSalariesFromDatabase() {
        String url = "jdbc:mysql://localhost:3306/prison_management_system";
        String user = "root";
        String password = "24mebredbms36";
        String query = "SELECT SUM(monthlySalary) AS totalSalaries FROM staff";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                return String.format("%.2f", rs.getDouble("totalSalaries"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return "Error retrieving data";
        }
        return "0.00";
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            String category = JOptionPane.showInputDialog(null, "Enter Expense Category:");
            String amount = JOptionPane.showInputDialog(null, "Enter Amount:");
            String date = JOptionPane.showInputDialog(null, "Enter Date (YYYY-MM-DD):");
            String notes = JOptionPane.showInputDialog(null, "Enter Notes:");

            if (category != null && amount != null && date != null && notes != null) {
                tableModel.addRow(new Object[]{category, amount, date, notes});
            }
        } else if (e.getSource() == editButton) {
            int selectedRow = budgetTable.getSelectedRow();
            if (selectedRow != -1) {
                String category = (String) tableModel.getValueAt(selectedRow, 0);
                String amount = (String) tableModel.getValueAt(selectedRow, 1);
                String date = (String) tableModel.getValueAt(selectedRow, 2);
                String notes = (String) tableModel.getValueAt(selectedRow, 3);

                category = JOptionPane.showInputDialog(null, "Edit Expense Category:", category);
                amount = JOptionPane.showInputDialog(null, "Edit Amount:", amount);
                date = JOptionPane.showInputDialog(null, "Edit Date:", date);
                notes = JOptionPane.showInputDialog(null, "Edit Notes:", notes);

                tableModel.setValueAt(category, selectedRow, 0);
                tableModel.setValueAt(amount, selectedRow, 1);
                tableModel.setValueAt(date, selectedRow, 2);
                tableModel.setValueAt(notes, selectedRow, 3);
            }
        } else if (e.getSource() == deleteButton) {
            int selectedRow = budgetTable.getSelectedRow();
            if (selectedRow != -1) {
                int confirmation = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this expense?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                if (confirmation == JOptionPane.YES_OPTION) {
                    tableModel.removeRow(selectedRow);
                }
            }
        }
    }
}