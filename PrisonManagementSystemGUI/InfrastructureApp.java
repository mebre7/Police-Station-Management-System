package PrisonManagementSystemGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

class Infrastructure {
    public static class Cell {
        private int cellNumber;
        private boolean isOccupied;

        public Cell(int cellNumber) {
            this.cellNumber = cellNumber;
            this.isOccupied = false;
        }

        public int getCellNumber() {
            return cellNumber;
        }

        public boolean isOccupied() {
            return isOccupied;
        }

        public void setOccupied(boolean occupied) {
            isOccupied = occupied;
        }
    }

    private List<Cell> cells;

    public Infrastructure() {
        cells = new ArrayList<>();
    }

    public void addCell(Cell cell) {
        cells.add(cell);
    }

    public List<Cell> getCells() {
        return cells;
    }
}

public class InfrastructureApp {
    private Infrastructure infrastructure;

    public InfrastructureApp() {
        infrastructure = new Infrastructure();
    }

    public JPanel createPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Title
        JLabel title = new JLabel("Prison Infrastructure Management", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(title, BorderLayout.NORTH);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10));

        JButton addCellButton = new JButton("Add Cell");
        JButton viewCellsButton = new JButton("View Cells");
        JButton clearDataButton = new JButton("Clear All Cells");

        buttonPanel.add(addCellButton);
        buttonPanel.add(viewCellsButton);
        buttonPanel.add(clearDataButton);

        panel.add(buttonPanel, BorderLayout.CENTER);

        // Action Listeners
        addCellButton.addActionListener(this::addCell);
        viewCellsButton.addActionListener(this::viewCells);
        clearDataButton.addActionListener(this::clearData);

        // Footer
        JLabel footer = new JLabel("Infrastructure Management", SwingConstants.CENTER);
        panel.add(footer, BorderLayout.SOUTH);

        return panel;
    }

    private void addCell(ActionEvent e) {
        String cellNumberStr = JOptionPane.showInputDialog("Enter Cell Number:");
        if (cellNumberStr != null) {
            try {
                int cellNumber = Integer.parseInt(cellNumberStr);
                Infrastructure.Cell cell = new Infrastructure.Cell(cellNumber);
                infrastructure.addCell(cell);
                JOptionPane.showMessageDialog(null, "Cell added successfully!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid number.");
            }
        }
    }

    private void viewCells(ActionEvent e) {
        StringBuilder cellInfo = new StringBuilder("Cells:\n");
        for (Infrastructure.Cell cell : infrastructure.getCells()) {
            cellInfo.append("Cell Number: ").append(cell.getCellNumber())
                    .append(", Occupied: ").append(cell.isOccupied()).append("\n");
        }
        if (cellInfo.length() == 6) { // No cells added
            cellInfo.append("No cells available.");
        }
        JOptionPane.showMessageDialog(null, cellInfo.toString());
    }

    private void clearData(ActionEvent e) {
        int confirmation = JOptionPane.showConfirmDialog(null, "Are you sure you want to clear all cell data?", "Clear All Data", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            infrastructure = new Infrastructure(); // Reset the infrastructure
            JOptionPane.showMessageDialog(null, "All cells have been cleared.");
        }
    }

    // To test, we can call this main method to display the Infrastructure panel in a frame.
    public static void main(String[] args) {
        JFrame frameInfra = new JFrame("Prison Infrastructure Management");
        InfrastructureApp app = new InfrastructureApp();
        frameInfra.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameInfra.setSize(500, 400);

        // Get the created panel and add it to the frameInfra
        frameInfra.add(app.createPanel());

        frameInfra.setVisible(true);
    }
}
