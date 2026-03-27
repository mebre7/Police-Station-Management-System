package PrisonManagementSystemGUI;

import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;

public class LogInPage2 {
    private IDandPasswords idandPasswords;

    public LogInPage2(IDandPasswords idandPasswords) {
        this.idandPasswords = idandPasswords;
    }

    public void createLoginPage() {
        // Create the frame1
        JFrame frame1 = new JFrame("Police Station Prison Management System - Login");
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame1.setExtendedState(JFrame.MAXIMIZED_BOTH); // This is just to me the frame responsive for the screen size
        frame1.setLayout(new BorderLayout());

        // Set the icon1 for the frame1
        ImageIcon icon1 = new ImageIcon("images/For_Project/Icon2.jpeg");
        frame1.setIconImage(icon1.getImage());

        // Background LoginPanel to hold the background image
        JPanel backgroundPane1 = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon background = new ImageIcon("images/For_Project/background.png"); // Adjust the path to your background image file
                g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPane1.setLayout(new BorderLayout());
        frame1.add(backgroundPane1, BorderLayout.CENTER);

        // Header LoginPanel for the title
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(44, 62, 80));
        headerPanel.setLayout(new BorderLayout());
        backgroundPane1.add(headerPanel, BorderLayout.NORTH);

        // Header label with title
        JLabel headerLabel = new JLabel("POLICE STATION PRISON MANAGEMENT SYSTEM!", SwingConstants.CENTER);
        headerLabel.setForeground(new Color(236, 240, 241)); // Light grey color
        headerLabel.setFont(new Font("Arial", Font.BOLD, 50));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        headerPanel.add(headerLabel, BorderLayout.CENTER);

        // Login LoginPanel for the rest of the content
        JPanel LoginPanel = new JPanel();
        LoginPanel.setBackground(new Color(44, 62, 80, 100)); // Semi-transparent background for the login LoginPanel
        LoginPanel.setLayout(new GridBagLayout()); // Use GridBagLayout for centering components
        backgroundPane1.add(LoginPanel, BorderLayout.CENTER);

        // Sub-LoginPanel to hold login components
        JPanel loginPanel = new JPanel();
        loginPanel.setBackground(new Color(44, 62, 80, 150)); // Semi-transparent background for login LoginPanel
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));

        // Welcome label with animation
        JLabel welcomeLabel = new JLabel("Welcome!");
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 32));
        welcomeLabel.setOpaque(false);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Add fade-in animation to the welcome label
        Timer fadeInTimer = new Timer(200, new ActionListener() {
            private float opacity = 0.0f;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (opacity < 1.0f) {
                    opacity += 0.05f;
                    welcomeLabel.setForeground(new Color(255, 255, 255, (int) (opacity * 255)));
                } else {
                    ((Timer) e.getSource()).stop(); // Stop the animation when it is done
                }
            }
        });
        fadeInTimer.start();

        // Title for login LoginPanel
        JLabel loginTitle = new JLabel("LOGIN");
        loginTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginTitle.setForeground(Color.RED);
        loginTitle.setFont(new Font("Arial", Font.BOLD, 24));
        loginPanel.add(welcomeLabel); // Add the "Welcome!" label first
        loginPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Add spacing
        loginPanel.add(loginTitle);

        loginPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Add spacing

        // Username field with placeholder
        JTextField usernameField = new JTextField("Username", 20);
        usernameField.setMaximumSize(new Dimension(300, 40));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.YELLOW),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        usernameField.setForeground(Color.YELLOW);
        usernameField.setToolTipText("Enter Username");

        // Focus listener to clear the placeholder when the user starts typing
        usernameField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (usernameField.getText().equals("Username")) {
                    usernameField.setText("");
                    usernameField.setForeground(Color.BLACK);
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (usernameField.getText().isEmpty()) {
                    usernameField.setText("Username");
                    usernameField.setForeground(Color.GRAY);
                }
            }
        });

        loginPanel.add(usernameField);

        loginPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add spacing

        // Panel to hold password field and eye icon1 button side by side
        JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0)); // Use FlowLayout for side-by-side
        passwordPanel.setBackground(new Color(44, 62, 80));

        // Password field with placeholder
        JPasswordField passwordField = new JPasswordField("Password", 20);
        passwordField.setMaximumSize(new Dimension(250, 40));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        passwordField.setForeground(Color.GRAY);
        passwordField.setToolTipText("Enter Password");

        // Focus listener to clear the placeholder when the user starts typing
        passwordField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (String.valueOf(passwordField.getPassword()).equals("Password")) {
                    passwordField.setText("");
                    passwordField.setForeground(Color.BLACK);
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (String.valueOf(passwordField.getPassword()).isEmpty()) {
                    passwordField.setText("Password");
                    passwordField.setForeground(Color.GRAY);
                }
            }
        });

        // Resize the icons to fit the password field height
        ImageIcon eyeClosedIcon = new ImageIcon(new ImageIcon("images/For_Project/eye_close.png").getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
        ImageIcon eyeOpenIcon = new ImageIcon(new ImageIcon("images/For_Project/eye_open.png").getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));

        // Show/Hide Password button with eye icon1
        JToggleButton showHidePasswordButton = new JToggleButton(eyeClosedIcon);  // Default eye-closed image
        showHidePasswordButton.setMaximumSize(new Dimension(40, 40));
        showHidePasswordButton.setBackground(new Color(44, 62, 80));
        showHidePasswordButton.setBorder(BorderFactory.createEmptyBorder());
        showHidePasswordButton.setContentAreaFilled(false);

        // Action listener to toggle password visibility
        showHidePasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (showHidePasswordButton.isSelected()) {
                    passwordField.setEchoChar((char) 0); // Show password
                    showHidePasswordButton.setIcon(eyeOpenIcon); // Switch to open eye icon1
                } else {
                    passwordField.setEchoChar('•'); // Hide password
                    showHidePasswordButton.setIcon(eyeClosedIcon); // Switch to closed eye icon1
                }
            }
        });

        passwordPanel.add(passwordField);  // Add the password field
        passwordPanel.add(showHidePasswordButton);  // Add the eye icon1 button

        loginPanel.add(passwordPanel);  // Add password LoginPanel to the login LoginPanel

        loginPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Add spacing


        // Login button
        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(41, 128, 185));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setMaximumSize(new Dimension(150, 40));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginPanel.add(loginButton);

        ////////////////
        // Maximum number of login attempts
        int[] attempts = {4}; // Use an array to allow modification inside the ActionListener

// Add action listener to login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                // Database credentials
                String url = "jdbc:mysql://localhost:3306/prison_management_system";
                String dbUser = "root";
                String dbPassword = "24mebredbms36";

                // Authenticate and fetch department
                try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword)) {
                    String query = "SELECT department FROM staff WHERE username = ? AND password = ?";
                    try (PreparedStatement stmt = conn.prepareStatement(query)) {
                        stmt.setString(1, username);
                        stmt.setString(2, password);

                        try (ResultSet rs = stmt.executeQuery()) {
                            if (rs.next()) {
                                String department = rs.getString("department");

                                // Redirect to the appropriate department page
                                switch (department) {
                                    case "Administrative/Wardens":
                                        new Administrative(idandPasswords); // Open the Administrative dashboard
                                        break;
                                    case "Maintenance Workers":
                                        new Maintenance(idandPasswords); // Open the Maintenance dashboard
                                        break;
                                    case "Guards":
                                        new Guards(idandPasswords); // Open the Guards dashboard
                                        break;
                                    case "Medical Staff":
                                        new Medical(idandPasswords); // Open the Medical Staff dashboard
                                        break;
                                    case "Rehabilitation Officers":
                                        new Rehabilitation(idandPasswords); // Open the Rehabilitation Officers dashboard
                                        break;
                                    default:
                                        JOptionPane.showMessageDialog(frame1, "Invalid department.", "Error", JOptionPane.ERROR_MESSAGE);
                                        break;
                                }
                            } else {
                                attempts[0]--; // Decrement the attempts counter
                                if (attempts[0] > 0) {
                                    JOptionPane.showMessageDialog(frame1, "Invalid credentials. You have " + attempts[0] + " attempts left.", "Error", JOptionPane.ERROR_MESSAGE);
                                } else {
                                    JOptionPane.showMessageDialog(frame1, "Maximum login attempts exceeded. You cannot proceed...", "Error", JOptionPane.ERROR_MESSAGE);
                                    frame1.dispose();
                                    System.exit(0);
                                }
                            }
                        } catch (ClassNotFoundException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(frame1, "Database connection failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        ///////////////
        LoginPanel.add(loginPanel); // Add login LoginPanel to the center LoginPanel
        // Footer label with the copyright message
        JLabel footerLabel = new JLabel("2025 Police Station Prison Management System", SwingConstants.CENTER);
        footerLabel.setForeground(new Color(149, 165, 166));
        footerLabel.setFont(new Font("Arial", Font.PLAIN, 12));

// Create About Us Button next to the footer
        JButton aboutUsButton = new JButton("About Us");
        aboutUsButton.setFont(new Font("Arial", Font.PLAIN, 12));
        aboutUsButton.setForeground(new Color(252, 24, 58));
        aboutUsButton.setBackground(Color.WHITE);

// Add action listener to show About Us panel when clicked
        aboutUsButton.addActionListener(e -> {
            // Create a new panel for About Us section
            JPanel aboutPanel = new JPanel();
            aboutPanel.setLayout(new BorderLayout());

            // Add text at the top
            JTextArea aboutText = new JTextArea("About Us: \n\n" +
                    "This project was developed as part of our coursework in Addis Ababa University using Java programming language. The aim is to create a Police Station Prison Management System that will help the authorities manage inmates and staff efficiently.\n\n" +
                    "The following students contributed to the development of this project.\n\n");
            aboutText.setEditable(false);
            aboutText.setLineWrap(true);
            aboutText.setWrapStyleWord(true);
            aboutText.setFont(new Font("Arial", Font.PLAIN, 20));
            aboutText.setBackground(Color.WHITE);
            aboutPanel.add(new JScrollPane(aboutText), BorderLayout.NORTH); // Add the text inside a scrollable area

            // Add the list of students below the text
            JPanel studentsPanel = new JPanel();
            studentsPanel.setLayout(new BoxLayout(studentsPanel, BoxLayout.Y_AXIS));

            String[] studentInfo = {
                    "1. EYOB MARIE",
                    "2. FIKREYESUS TESFAYE",
                    "3. HALELUYA SAMUEL",
                    "4. MEBRATU CHEKA",
                    "5. MINASE TILAYE",
                    "6. NEHEMIAH ADANE",
                    "7. NAOL NEGATU",
                    "8. TIGIST AGONAFER"
            };
            for (String student : studentInfo) {
                studentsPanel.add(new JLabel(student)); // Add each student as a label in the panel
            }

            aboutPanel.add(studentsPanel, BorderLayout.CENTER); // Add student list to center of the panel

            // Add university logo (replace the image path with your actual logo)
            JPanel logoPanel = new JPanel();
            ImageIcon universityLogo = new ImageIcon("images/For_Project/AAUlogo.jpeg");
            JLabel logoLabel = new JLabel(universityLogo);
            logoPanel.add(logoLabel);
            aboutPanel.add(logoPanel, BorderLayout.SOUTH);

            // Create Back button to return to the main frame
            JButton backButton = new JButton("Back");
            backButton.addActionListener(backEvent -> {
                // Return to the main frame when clicked
                frame1.setContentPane(backgroundPane1); // Replace with the main panel (frameAd’s content panel)
                frame1.revalidate();
                frame1.repaint();
            });

            // Add Back button to bottom of the panel
            aboutPanel.add(backButton, BorderLayout.SOUTH);

            // Replace the current content pane with the About Us panel
            frame1.setContentPane(aboutPanel);
            frame1.revalidate();
            frame1.repaint();
        });

// Create a panel for the footer that includes both the footer label and the About Us button
        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new BorderLayout());
        footerPanel.add(footerLabel, BorderLayout.CENTER);
        footerPanel.add(aboutUsButton, BorderLayout.EAST); // Position the About Us button to the right

        frame1.add(footerPanel, BorderLayout.SOUTH);
        ////////////
        frame1.setVisible(true);
    }
        public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                IDandPasswords idandPasswords = new IDandPasswords();
                LogInPage2 loginPage = new LogInPage2(idandPasswords);
                loginPage.createLoginPage(); // Launch the login page
            }
        });
    }
}