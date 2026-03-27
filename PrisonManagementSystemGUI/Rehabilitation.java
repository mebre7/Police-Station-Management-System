package PrisonManagementSystemGUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;

public class Rehabilitation {
    private JFrame frameRe;
    private JPanel rightPanel;
    private CardLayout cardLayout;

    public Rehabilitation(IDandPasswords idandPasswords) throws ClassNotFoundException {
        frameRe = new JFrame("Prison Management System - Rehabilitation Officers");
        frameRe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameRe.setExtendedState(JFrame.MAXIMIZED_BOTH);
        ImageIcon icon1 = new ImageIcon("images/For_Project/Icon2.jpeg");
        frameRe.setIconImage(icon1.getImage());

        // Left Panel for Options
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setPreferredSize(new Dimension(200, frameRe.getHeight()));
        leftPanel.setBackground(Color.LIGHT_GRAY);

        InfrastructureApp infrastructureApp = new InfrastructureApp();

        JButton inmateInfoAdBtn = new JButton("Inmate Information");
        JButton exitButtonRe = new JButton("Exit");
        JButton backButtonRe = new JButton("Back");

        inmateInfoAdBtn.addActionListener(e -> cardLayout.show(rightPanel, "InmatePanel"));

        backButtonRe.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameRe.dispose();
                new LogInPage2(idandPasswords);
            }
        });
        exitButtonRe.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        leftPanel.add(inmateInfoAdBtn);
        leftPanel.add(backButtonRe);
        leftPanel.add(exitButtonRe);

        // Right Panel for Content
        rightPanel = new JPanel();
        cardLayout = new CardLayout();
        rightPanel.setLayout(cardLayout);

        // Add default blue background panel
        JPanel defaultPanel = new JPanel();
        defaultPanel.setBackground(new Color(34, 75, 114));
        rightPanel.add(defaultPanel, "DefaultPanel");

        rightPanel.add(createInmatePanel(), "InmatePanel");
        rightPanel.add(infrastructureApp.createPanel(), "InfrastructurePanel");


        // Show default panel on startup
        cardLayout.show(rightPanel, "DefaultPanel");

        // Main Layout
        frameRe.setLayout(new BorderLayout());
        frameRe.add(leftPanel, BorderLayout.WEST);
        frameRe.add(rightPanel, BorderLayout.CENTER);

        frameRe.setVisible(true);
    }

    private JPanel createInmatePanel() throws ClassNotFoundException {
        JPanel inmatePanel = new JPanel();
        inmatePanel.setLayout(new BorderLayout());

        // Create table for inmate information
        JTable table = createTable("SELECT * FROM inmates",
                new String[]{"rollNo", "id", "first_name", "middle_name", "last_name", "sex", "age", "memberType",
                        "cellNumber", "crimeCommitted", "sentenceDuration", "datePrisoned", "releaseDate"});

        // Add double-click functionality to view profile
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row != -1) {
                        String inmateId = table.getValueAt(row, 1).toString();
                        String fullName = table.getValueAt(row, 2) + " " + table.getValueAt(row, 3) + " " + table.getValueAt(row, 4);
                        String sex = table.getValueAt(row, 5).toString();
                        int age = Integer.parseInt(table.getValueAt(row, 6).toString());
                        int cellNumber = Integer.parseInt(table.getValueAt(row, 8).toString());
                        String crimeCommitted = table.getValueAt(row, 9).toString();
                        int sentenceDuration = Integer.parseInt(table.getValueAt(row, 10).toString());
                        LocalDate datePrisoned = ((java.sql.Date) table.getValueAt(row, 11)).toLocalDate();
                        LocalDate releaseDate = ((java.sql.Date) table.getValueAt(row, 12)).toLocalDate();

                        showInmateProfile(inmateId, fullName, sex, age, cellNumber, crimeCommitted, sentenceDuration, datePrisoned, releaseDate);
                    }
                }
            }
        });

        // Scroll pane for the table
        JScrollPane scrollPane = new JScrollPane(table);
        inmatePanel.add(scrollPane, BorderLayout.CENTER);

        // Footer panel for buttons
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding

        // Search and Add buttons
        JTextField searchField = new JTextField(15);
        JButton searchButton = new JButton("Search");

        footerPanel.add(new JLabel("Search by ID or First Name:"));
        footerPanel.add(searchField);
        footerPanel.add(searchButton);
        searchButton.addActionListener(e -> {
            String searchText = searchField.getText();
            if (!searchText.isEmpty()) {
                boolean found = false;
                for (int row = 0; row < table.getRowCount(); row++) {
                    String id = table.getValueAt(row, 1).toString();
                    String firstName = table.getValueAt(row, 2).toString();
                    if (id.equals(searchText) || firstName.equalsIgnoreCase(searchText)) {
                        table.setRowSelectionInterval(row, row);
                        table.scrollRectToVisible(table.getCellRect(row, 0, true));
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    JOptionPane.showMessageDialog(null, "No inmate found with ID or First Name: " + searchText, "Search Result", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please enter an ID or First Name to search.", "Input Error", JOptionPane.WARNING_MESSAGE);
            }
        });
        // Add footer panel to inmate panel
        inmatePanel.add(footerPanel, BorderLayout.SOUTH);

        return inmatePanel;
    }


    private JTable createTable(String query, String[] columnNames) {
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Makes the table non-editable
            }
        };
        JTable table = new JTable(tableModel);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/prison_management_system", "root", "24mebredbms36");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Object[] rowData = new Object[columnNames.length];
                for (int i = 0; i < columnNames.length; i++) {
                    rowData[i] = rs.getObject(i + 1);
                }
                tableModel.addRow(rowData);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frameRe, "Database error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        return table;
    }

    private void showInmateProfile(String inmateId, String fullName, String sex, int age, int cellNumber,
                                   String crimeCommitted, int sentenceDuration, LocalDate datePrisoned, LocalDate releaseDate) {
        JPanel profilePanel = new JPanel();
        profilePanel.setLayout(new BorderLayout());

        // Photo frame (empty) at the top-center
        JPanel photoPanel = new JPanel();
        photoPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        photoPanel.add(new JLabel("Photo Frame")); // This can be replaced with a photo later
        profilePanel.add(photoPanel, BorderLayout.NORTH);

        // Information below the photo
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(0, 1)); // One column for information

        infoPanel.add(new JLabel("Inmate ID: " + inmateId));
        infoPanel.add(new JLabel("Full Name: " + fullName));
        infoPanel.add(new JLabel("Sex: " + sex));
        infoPanel.add(new JLabel("Age: " + age + " years old"));
        infoPanel.add(new JLabel("Cell Number: " + cellNumber));
        infoPanel.add(new JLabel("Crime Committed: " + crimeCommitted));
        infoPanel.add(new JLabel("Sentence Duration: " + sentenceDuration + " years"));
        infoPanel.add(new JLabel("Date Prisoned: " + datePrisoned));
        infoPanel.add(new JLabel("Release Date: " + releaseDate));

        profilePanel.add(infoPanel, BorderLayout.CENTER);

        // Create a frame or dialog to show the profile
        JFrame profileFrame = new JFrame("Inmate Profile");
        profileFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        profileFrame.add(profilePanel);
        profileFrame.setSize(400, 500); // Adjust the size as needed
        profileFrame.setVisible(true);
    }
    //No need of main runnable class here
}