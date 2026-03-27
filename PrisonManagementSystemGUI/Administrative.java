package PrisonManagementSystemGUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;

public class Administrative {
    private JFrame frameAd;
    private JPanel rightPanel;
    private CardLayout cardLayout;

    public Administrative(IDandPasswords idandPasswords) throws ClassNotFoundException {
        frameAd = new JFrame("Prison Management System - Administrative/Wardens");
        frameAd.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameAd.setExtendedState(JFrame.MAXIMIZED_BOTH);
        ImageIcon icon1 = new ImageIcon("images/For_Project/Icon2.jpeg");
        frameAd.setIconImage(icon1.getImage());

        // Left Panel for Options
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setPreferredSize(new Dimension(200, frameAd.getHeight()));
        leftPanel.setBackground(Color.LIGHT_GRAY);

        InfrastructureApp infrastructureApp = new InfrastructureApp();
        ScheduleGUI scheduleGUI = new ScheduleGUI();
        ReportGUI reportGUI = new ReportGUI();
        BudgetGUI budgetGUI = new BudgetGUI();

        JButton inmateInfoAdBtn = new JButton("Inmate Information");
        JButton staffInfoAdBtn = new JButton("Staff Information");
        JButton infraAdBtn = new JButton("Infrastructure");
        JButton scheduleAdBtn = new JButton("Schedule");
        JButton reportAdBtn = new JButton("Report");
        JButton budgetAdBtn = new JButton("Budget");
        JButton backButtonAd = new JButton("Back");
        JButton exitButtonAd = new JButton("Exit");

        inmateInfoAdBtn.addActionListener(e -> cardLayout.show(rightPanel, "InmatePanel"));
        staffInfoAdBtn.addActionListener(e -> cardLayout.show(rightPanel, "StaffPanel"));
        staffInfoAdBtn.addActionListener(e -> cardLayout.show(rightPanel, "StaffPanel"));
        infraAdBtn.addActionListener(e -> cardLayout.show(rightPanel, "InfrastructurePanel"));
        scheduleAdBtn.addActionListener(e -> cardLayout.show(rightPanel, "SchedulePanel"));
        reportAdBtn.addActionListener(e -> cardLayout.show(rightPanel, "ReportPanel"));
        budgetAdBtn.addActionListener(e -> cardLayout.show(rightPanel, "BudgetPanel"));
        backButtonAd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameAd.dispose();
                new LogInPage2(idandPasswords);
            }
        });
        exitButtonAd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        leftPanel.add(inmateInfoAdBtn);
        leftPanel.add(staffInfoAdBtn);
        leftPanel.add(infraAdBtn);
        leftPanel.add(scheduleAdBtn);
        leftPanel.add(reportAdBtn);
        leftPanel.add(budgetAdBtn);
        leftPanel.add(backButtonAd);
        leftPanel.add(exitButtonAd);

        // Right Panel for Content
        rightPanel = new JPanel();
        cardLayout = new CardLayout();
        rightPanel.setLayout(cardLayout);

        // Add default blue background panel
        JPanel defaultPanel = new JPanel();
        defaultPanel.setBackground(new Color(34, 75, 114));
        rightPanel.add(defaultPanel, "DefaultPanel");

        rightPanel.add(createInmatePanel(), "InmatePanel");
        rightPanel.add(createStaffPanel(), "StaffPanel");
        rightPanel.add(infrastructureApp.createPanel(), "InfrastructurePanel");
        rightPanel.add(scheduleGUI.createPanel(), "SchedulePanel");
        rightPanel.add(reportGUI.createPanel(), "ReportPanel");
        rightPanel.add(budgetGUI.createPanel(), "BudgetPanel");


        // Show default panel on startup
        cardLayout.show(rightPanel, "DefaultPanel");

        // Main Layout
        frameAd.setLayout(new BorderLayout());
        frameAd.add(leftPanel, BorderLayout.WEST);
        frameAd.add(rightPanel, BorderLayout.CENTER);

        frameAd.setVisible(true);
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
        JButton addButton = new JButton("Add");

        footerPanel.add(new JLabel("Search by ID or First Name:"));
        footerPanel.add(searchField);
        footerPanel.add(searchButton);
        footerPanel.add(addButton);

        // Add functionality to Search button
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

        // Add functionality to Add button
        addButton.addActionListener(e -> showAddInmatePanel());

        // Add footer panel to inmate panel
        inmatePanel.add(footerPanel, BorderLayout.SOUTH);

        return inmatePanel;
    }

    private void showAddInmatePanel() {
        JFrame addFrame = new JFrame("Add Inmate");
        addFrame.setSize(400, 600);
        addFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addFrame.setLayout(new BorderLayout());
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(0, 2, 10, 10)); // 2 columns for labels and text fields

        // Form fields
        JTextField idField = new JTextField();
        JTextField firstNameField = new JTextField();
        JTextField middleNameField = new JTextField();
        JTextField lastNameField = new JTextField();
        JTextField sexField = new JTextField();
        JTextField ageField = new JTextField();
        JTextField memberTypeField = new JTextField();
        JTextField cellNumberField = new JTextField();
        JTextField crimeCommittedField = new JTextField();
        JTextField sentenceDurationField = new JTextField();
        JTextField datePrisonedField = new JTextField();
        JTextField releaseDateField = new JTextField();

        formPanel.add(new JLabel("ID:"));
        formPanel.add(idField);
        formPanel.add(new JLabel("First Name:"));
        formPanel.add(firstNameField);
        formPanel.add(new JLabel("Middle Name:"));
        formPanel.add(middleNameField);
        formPanel.add(new JLabel("Last Name:"));
        formPanel.add(lastNameField);
        formPanel.add(new JLabel("Sex:"));
        formPanel.add(sexField);
        formPanel.add(new JLabel("Age:"));
        formPanel.add(ageField);
        formPanel.add(new JLabel("Member Type:"));
        formPanel.add(memberTypeField);
        formPanel.add(new JLabel("Cell Number:"));
        formPanel.add(cellNumberField);
        formPanel.add(new JLabel("Crime Committed:"));
        formPanel.add(crimeCommittedField);
        formPanel.add(new JLabel("Sentence Duration:"));
        formPanel.add(sentenceDurationField);
        formPanel.add(new JLabel("Date Prisoned (yyyy-mm-dd):"));
        formPanel.add(datePrisonedField);
        formPanel.add(new JLabel("Release Date (yyyy-mm-dd):"));
        formPanel.add(releaseDateField);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            addFrame.dispose();
            int confirmation = JOptionPane.showConfirmDialog(addFrame, "Are you sure you want to add this inmate?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirmation == JOptionPane.YES_OPTION) {
                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/prison_management_system", "root", "24mebredbms36");
                     PreparedStatement pstmt = conn.prepareStatement("INSERT INTO inmates (id, first_name, middle_name, last_name, sex, age, memberType, cellNumber, crimeCommitted, sentenceDuration, datePrisoned, releaseDate) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {

                    pstmt.setString(1, idField.getText());
                    pstmt.setString(2, firstNameField.getText());
                    pstmt.setString(3, middleNameField.getText());
                    pstmt.setString(4, lastNameField.getText());
                    pstmt.setString(5, sexField.getText());
                    pstmt.setInt(6, Integer.parseInt(ageField.getText()));
                    pstmt.setString(7, memberTypeField.getText());
                    pstmt.setInt(8, Integer.parseInt(cellNumberField.getText()));
                    pstmt.setString(9, crimeCommittedField.getText());
                    pstmt.setInt(10, Integer.parseInt(sentenceDurationField.getText()));
                    pstmt.setDate(11, java.sql.Date.valueOf(datePrisonedField.getText()));
                    pstmt.setDate(12, java.sql.Date.valueOf(releaseDateField.getText()));

                    pstmt.executeUpdate();
                    JOptionPane.showMessageDialog(addFrame, "Inmate added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    addFrame.dispose();
                } catch (SQLException | NumberFormatException ex) {
                    JOptionPane.showMessageDialog(addFrame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(submitButton);

        addFrame.add(formPanel, BorderLayout.CENTER);
        addFrame.add(buttonPanel, BorderLayout.SOUTH);
        addFrame.setVisible(true);
    }


    private JPanel createStaffPanel() throws ClassNotFoundException {
        JPanel staffPanel = new JPanel();
        staffPanel.setLayout(new BorderLayout());

        // Create the table for displaying staff information
        JTable table = createTable("SELECT * FROM staff",
                new String[]{"rollNo", "id", "first_name", "middle_name", "last_name", "sex", "age", "memberType",
                        "department", "role", "dutyShift", "hireDate", "monthlySalary", "address",
                        "contactNumber", "username", "password"});

        // Add mouse listener for double-click functionality to view profile
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row != -1) {
                        String staffId = table.getValueAt(row, 1).toString();
                        String fullName = table.getValueAt(row, 2) + " " + table.getValueAt(row, 3) + " " + table.getValueAt(row, 4);
                        String sex = table.getValueAt(row, 5).toString();
                        int age = Integer.parseInt(table.getValueAt(row, 6).toString());
                        String department = table.getValueAt(row, 8).toString();
                        String role = table.getValueAt(row, 9).toString();
                        String dutyShift = table.getValueAt(row, 10).toString();
                        LocalDate hireDate = ((java.sql.Date) table.getValueAt(row, 11)).toLocalDate();
                        double monthlySalary = Double.parseDouble(table.getValueAt(row, 12).toString());
                        String address = table.getValueAt(row, 13).toString();
                        String contactNumber = table.getValueAt(row, 14).toString();

                        showStaffProfile(staffId, fullName, sex, age, department, role, dutyShift, hireDate.toString(),
                                monthlySalary, address, contactNumber);
                    }
                }
            }
        });

        // Scroll pane for the table
        JScrollPane scrollPane = new JScrollPane(table);
        staffPanel.add(scrollPane, BorderLayout.CENTER);

        // Footer panel for Search and Add buttons
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding

        // Create search field and buttons
        JTextField searchField = new JTextField(15);
        JButton searchButton = new JButton("Search");
        JButton addButton = new JButton("Add");

        footerPanel.add(new JLabel("Search by ID or First Name:"));
        footerPanel.add(searchField);
        footerPanel.add(searchButton);
        footerPanel.add(addButton);

        // Add functionality to Search button
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
                    JOptionPane.showMessageDialog(null, "No staff found with ID or First Name: " + searchText, "Search Result", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please enter an ID or First Name to search.", "Input Error", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Add functionality to Add button
        addButton.addActionListener(e -> showAddStaffPanel());

        // Add footer panel to staff panel
        staffPanel.add(footerPanel, BorderLayout.SOUTH);

        return staffPanel;
    }

    private void showAddStaffPanel() {
        JFrame addFrame = new JFrame("Add Staff");
        addFrame.setSize(400, 600);
        addFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addFrame.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(0, 2, 10, 10)); // 2 columns for labels and text fields

        // Form fields
        JTextField idField = new JTextField();
        JTextField firstNameField = new JTextField();
        JTextField middleNameField = new JTextField();
        JTextField lastNameField = new JTextField();
        JTextField sexField = new JTextField();
        JTextField ageField = new JTextField();
        JTextField memberTypeField = new JTextField();
        JTextField departmentField = new JTextField();
        JTextField roleField = new JTextField();
        JTextField dutyShiftField = new JTextField();
        JTextField hireDateField = new JTextField();
        JTextField monthlySalaryField = new JTextField();
        JTextField addressField = new JTextField();
        JTextField contactNumberField = new JTextField();
        JTextField usernameField = new JTextField();
        JTextField passwordField = new JTextField();

        formPanel.add(new JLabel("ID:"));
        formPanel.add(idField);
        formPanel.add(new JLabel("First Name:"));
        formPanel.add(firstNameField);
        formPanel.add(new JLabel("Middle Name:"));
        formPanel.add(middleNameField);
        formPanel.add(new JLabel("Last Name:"));
        formPanel.add(lastNameField);
        formPanel.add(new JLabel("Sex:"));
        formPanel.add(sexField);
        formPanel.add(new JLabel("Age:"));
        formPanel.add(ageField);
        formPanel.add(new JLabel("Member Type:"));
        formPanel.add(memberTypeField);
        formPanel.add(new JLabel("Department:"));
        formPanel.add(departmentField);
        formPanel.add(new JLabel("Role:"));
        formPanel.add(roleField);
        formPanel.add(new JLabel("Duty Shift:"));
        formPanel.add(dutyShiftField);
        formPanel.add(new JLabel("Hire Date (yyyy-mm-dd):"));
        formPanel.add(hireDateField);
        formPanel.add(new JLabel("Monthly Salary:"));
        formPanel.add(monthlySalaryField);
        formPanel.add(new JLabel("Address:"));
        formPanel.add(addressField);
        formPanel.add(new JLabel("Contact Number:"));
        formPanel.add(contactNumberField);
        formPanel.add(new JLabel("Username:"));
        formPanel.add(usernameField);
        formPanel.add(new JLabel("Password:"));
        formPanel.add(passwordField);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            addFrame.dispose();
            int confirmation = JOptionPane.showConfirmDialog(addFrame, "Are you sure you want to add this staff?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirmation == JOptionPane.YES_OPTION) {
                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/prison_management_system", "root", "24mebredbms36");
                     PreparedStatement pstmt = conn.prepareStatement("INSERT INTO staff (id, first_name, middle_name, last_name, sex, age, memberType, department, role, dutyShift, hireDate, monthlySalary, address, contactNumber, username, password) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {

                    pstmt.setString(1, idField.getText());
                    pstmt.setString(2, firstNameField.getText());
                    pstmt.setString(3, middleNameField.getText());
                    pstmt.setString(4, lastNameField.getText());
                    pstmt.setString(5, sexField.getText());
                    pstmt.setInt(6, Integer.parseInt(ageField.getText()));
                    pstmt.setString(7, memberTypeField.getText());
                    pstmt.setString(8, departmentField.getText());
                    pstmt.setString(9, roleField.getText());
                    pstmt.setString(10, dutyShiftField.getText());
                    pstmt.setDate(11, java.sql.Date.valueOf(hireDateField.getText()));
                    pstmt.setDouble(12, Double.parseDouble(monthlySalaryField.getText()));
                    pstmt.setString(13, addressField.getText());
                    pstmt.setString(14, contactNumberField.getText());
                    pstmt.setString(15, usernameField.getText());
                    pstmt.setString(16, passwordField.getText());

                    pstmt.executeUpdate();
                    JOptionPane.showMessageDialog(addFrame, "Staff added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    addFrame.dispose();
                } catch (SQLException | NumberFormatException ex) {
                    JOptionPane.showMessageDialog(addFrame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(submitButton);

        addFrame.add(formPanel, BorderLayout.CENTER);
        addFrame.add(buttonPanel, BorderLayout.SOUTH);
        addFrame.setVisible(true);
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
            JOptionPane.showMessageDialog(frameAd, "Database error: " + ex.getMessage(),
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

        // Footer with buttons
        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JButton editButton = new JButton("Edit Inmate Info");
        JButton deleteButton = new JButton("Delete");
        footerPanel.add(editButton);
        footerPanel.add(deleteButton);
        // Edit button action
        editButton.addActionListener(e -> showInmateEditForm(inmateId, fullName, sex, age, cellNumber, crimeCommitted, sentenceDuration, datePrisoned, releaseDate));

        // Delete button action
        deleteButton.addActionListener(e -> deleteInmate(inmateId));

        footerPanel.add(editButton);
        footerPanel.add(deleteButton);

        profilePanel.add(footerPanel, BorderLayout.SOUTH);

        // Create a frame or dialog to show the profile
        JFrame profileFrame = new JFrame("Inmate Profile");
        profileFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        profileFrame.add(profilePanel);
        profileFrame.setSize(400, 500); // Adjust the size as needed
        profileFrame.setVisible(true);
    }

    private void showInmateEditForm(String inmateId, String fullName, String sex, int age, int cellNumber,
                                    String crimeCommitted, int sentenceDuration, LocalDate datePrisoned, LocalDate releaseDate) {
        JPanel editPanel = new JPanel(new GridLayout(10, 2));

        // Create text fields and set them with current values
        JTextField fullNameField = new JTextField(fullName);
        JTextField sexField = new JTextField(sex);
        JTextField ageField = new JTextField(String.valueOf(age));
        JTextField cellNumberField = new JTextField(String.valueOf(cellNumber));
        JTextField crimeField = new JTextField(crimeCommitted);
        JTextField sentenceField = new JTextField(String.valueOf(sentenceDuration));
        JTextField prisonDateField = new JTextField(datePrisoned.toString());
        JTextField releaseDateField = new JTextField(releaseDate.toString());

        // Save button to update the inmate details
        JButton saveButton = new JButton("Save Changes");
        saveButton.addActionListener(e -> {

            // Input validation before updating
            try {
                int parsedAge = Integer.parseInt(ageField.getText());
                int parsedCellNumber = Integer.parseInt(cellNumberField.getText());
                int parsedSentence = Integer.parseInt(sentenceField.getText());

                LocalDate parsedPrisonDate = LocalDate.parse(prisonDateField.getText());
                LocalDate parsedReleaseDate = LocalDate.parse(releaseDateField.getText());

                // Call the update method
                updateInmate(inmateId, fullNameField.getText(), sexField.getText(), parsedAge, parsedCellNumber,
                        crimeField.getText(), parsedSentence, parsedPrisonDate, parsedReleaseDate);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter valid numbers for age, cell number, and sentence duration.",
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Add labels and fields to the panel
        editPanel.add(new JLabel("Full Name"));
        editPanel.add(fullNameField);
        editPanel.add(new JLabel("Sex"));
        editPanel.add(sexField);
        editPanel.add(new JLabel("Age"));
        editPanel.add(ageField);
        editPanel.add(new JLabel("Cell Number"));
        editPanel.add(cellNumberField);
        editPanel.add(new JLabel("Crime Committed"));
        editPanel.add(crimeField);
        editPanel.add(new JLabel("Sentence Duration (years)"));
        editPanel.add(sentenceField);
        editPanel.add(new JLabel("Date Prisoned"));
        editPanel.add(prisonDateField);
        editPanel.add(new JLabel("Release Date"));
        editPanel.add(releaseDateField);

        // Spacer and Save button
        editPanel.add(new JLabel());
        editPanel.add(saveButton);

        JFrame editFrame = new JFrame("Edit Inmate Info");
        editFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        editFrame.add(editPanel);
        editFrame.setSize(300, 400);
        editFrame.setVisible(true);
    }
    private void updateInmate(String inmateId, String fullName, String sex, int age, int cellNumber,
                              String crimeCommitted, int sentenceDuration, LocalDate datePrisoned, LocalDate releaseDate) {
        String updateQuery = "UPDATE inmates SET first_name = ?, middle_name = ?, last_name = ?, sex = ?, age = ?, cellNumber = ?, " +
                "crimeCommitted = ?, sentenceDuration = ?, datePrisoned = ?, releaseDate = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/prison_management_system", "root", "24mebredbms36");
             PreparedStatement ps = conn.prepareStatement(updateQuery)) {

            ps.setString(1, fullName);
            ps.setString(2, sex);
            ps.setInt(3, age);
            ps.setInt(4, cellNumber);
            ps.setString(5, crimeCommitted);
            ps.setInt(6, sentenceDuration);
            ps.setDate(7, Date.valueOf(datePrisoned));  // Convert LocalDate to java.sql.Date
            ps.setDate(8, Date.valueOf(releaseDate));   // Convert LocalDate to java.sql.Date
            ps.setString(9, inmateId);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Inmate information updated successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Error: Inmate information could not be updated.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void deleteInmate(String inmateId) {
        String deleteQuery = "DELETE FROM inmates WHERE id = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/prison_management_system", "root", "24mebredbms36");
             PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) {
            pstmt.setString(1, inmateId);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(frameAd, "Inmate deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frameAd, "Error deleting inmate: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showStaffProfile(String staffId, String fullName, String sex, int age, String department, String role,
                                  String dutyShift, String hireDate, double monthlySalary, String address, String contactNumber) {
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

        infoPanel.add(new JLabel("Staff ID: " + staffId));
        infoPanel.add(new JLabel("Full Name: " + fullName));
        infoPanel.add(new JLabel("Sex: " + sex));
        infoPanel.add(new JLabel("Age: " + age + " years old"));
        infoPanel.add(new JLabel("Department: " + department));
        infoPanel.add(new JLabel("Role: " + role));
        infoPanel.add(new JLabel("Duty Shift: " + dutyShift));
        infoPanel.add(new JLabel("Hire Date: " + hireDate));
        infoPanel.add(new JLabel("Monthly Salary: ETB " + monthlySalary));
        infoPanel.add(new JLabel("Address: " + address));
        infoPanel.add(new JLabel("Contact Number: " + contactNumber));

        profilePanel.add(infoPanel, BorderLayout.CENTER);

        // Footer with buttons
        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JButton editButton = new JButton("Edit Staff Info");
        JButton deleteButton = new JButton("Delete");
        footerPanel.add(editButton);
        footerPanel.add(deleteButton);
        // Edit button action
        editButton.addActionListener(e -> showStaffEditForm(staffId, fullName, sex, age, department, role, dutyShift, hireDate, monthlySalary, address, contactNumber));

        // Delete button action
        deleteButton.addActionListener(e -> deleteStaff(staffId));

        footerPanel.add(editButton);
        footerPanel.add(deleteButton);

        profilePanel.add(footerPanel, BorderLayout.SOUTH);

        // Create a frame or dialog to show the profile
        JFrame profileFrame = new JFrame("Staff Profile");
        profileFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        profileFrame.add(profilePanel);
        profileFrame.setSize(400, 600); // Adjust the size as needed
        profileFrame.setVisible(true);
    }
    private void showStaffEditForm(String staffId, String fullName, String sex, int age, String department, String role,
                                   String dutyShift, String hireDate, double monthlySalary, String address, String contactNumber) {
        JPanel editPanel = new JPanel(new GridLayout(12, 2));

        // Create text fields and set them with current values
        JTextField fullNameField = new JTextField(fullName);
        JTextField sexField = new JTextField(sex);
        JTextField ageField = new JTextField(String.valueOf(age));
        JTextField departmentField = new JTextField(department);
        JTextField roleField = new JTextField(role);
        JTextField dutyShiftField = new JTextField(dutyShift);
        JTextField hireDateField = new JTextField(hireDate);
        JTextField salaryField = new JTextField(String.valueOf(monthlySalary));
        JTextField addressField = new JTextField(address);
        JTextField contactField = new JTextField(contactNumber);

        // Save button to update the staff details
        JButton saveButton = new JButton("Save Changes");
        saveButton.addActionListener(e -> {
            // Input validation before updating
            try {
                int parsedAge = Integer.parseInt(ageField.getText());
                double parsedSalary = Double.parseDouble(salaryField.getText());

                // Call the update method
                updateStaff(staffId, fullNameField.getText(), sexField.getText(), parsedAge, departmentField.getText(),
                        roleField.getText(), dutyShiftField.getText(), hireDateField.getText(), parsedSalary,
                        addressField.getText(), contactField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter valid numbers for age and monthly salary.",
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Add labels and fields to the panel
        editPanel.add(new JLabel("Full Name"));
        editPanel.add(fullNameField);
        editPanel.add(new JLabel("Sex"));
        editPanel.add(sexField);
        editPanel.add(new JLabel("Age"));
        editPanel.add(ageField);
        editPanel.add(new JLabel("Department"));
        editPanel.add(departmentField);
        editPanel.add(new JLabel("Role"));
        editPanel.add(roleField);
        editPanel.add(new JLabel("Duty Shift"));
        editPanel.add(dutyShiftField);
        editPanel.add(new JLabel("Hire Date"));
        editPanel.add(hireDateField);
        editPanel.add(new JLabel("Monthly Salary"));
        editPanel.add(salaryField);
        editPanel.add(new JLabel("Address"));
        editPanel.add(addressField);
        editPanel.add(new JLabel("Contact Number"));
        editPanel.add(contactField);

        // Spacer and Save button
        editPanel.add(new JLabel());
        editPanel.add(saveButton);

        JFrame editFrame = new JFrame("Edit Staff Info");
        editFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        editFrame.add(editPanel);
        editFrame.setSize(300, 400);
        editFrame.setVisible(true);
    }
    private void updateStaff(String staffId, String fullName, String sex, int age, String department, String role,
                             String dutyShift, String hireDate, double monthlySalary, String address, String contactNumber) {
        String updateQuery = "UPDATE staff SET first_name = ?, middle_name = ?, last_name = ?, sex = ?, age = ?, department = ?, role = ?, dutyShift = ?, " +
                "hireDate = ?, monthlySalary = ?, address = ?, contactNumber = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/prison_management_system", "root", "24mebredbms36");
             PreparedStatement ps = conn.prepareStatement(updateQuery)) {

            ps.setString(1, fullName);
            ps.setString(2, sex);
            ps.setInt(3, age);
            ps.setString(4, department);
            ps.setString(5, role);
            ps.setString(6, dutyShift);
            ps.setDate(7, Date.valueOf(hireDate));  // Convert LocalDate (String format) to java.sql.Date
            ps.setDouble(8, monthlySalary);
            ps.setString(9, address);
            ps.setString(10, contactNumber);
            ps.setString(11, staffId);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Staff information updated successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Error: Staff information could not be updated.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteStaff(String staffId) {
        String deleteQuery = "DELETE FROM staff WHERE id = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/prison_management_system", "root", "24mebredbms36");
             PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) {
            pstmt.setString(1, staffId);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(frameAd, "Staff deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frameAd, "Error deleting staff: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    //No need of main runnable class here
}