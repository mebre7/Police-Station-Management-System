package PrisonManagementSystemGUI;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class IDandPasswords {
    private final Map<String, String> loginInfo;

    public IDandPasswords() {
        loginInfo = new HashMap<>();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Establishing a connection to the database
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/prison_management_system", "root", "24mebredbms36");
            Statement statement = connection.createStatement();
            String query = "SELECT username, password FROM staff";
            ResultSet resultSet = statement.executeQuery(query);

            // Load login data from the database
            while (resultSet.next()) {
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                loginInfo.put(username, password);
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, String> getLoginInfo() {
        return loginInfo;
    }

    // Method to check if username and password match
    public boolean authenticate(String username, String password) {
        return loginInfo.containsKey(username) && loginInfo.get(username).equals(password);
    }
}
