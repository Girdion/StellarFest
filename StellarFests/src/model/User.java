package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import util.Database;

public class User {
    private String user_id;
    private String username;
    private String password;
    private String email;
    private String role;

    private Database connect;

    public User() {
        connect = Database.getInstance();
    }

    public User(String user_id, String username, String password, String email, String role) {
        this.user_id = user_id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public Vector<User> getAllUsers() {
        Vector<User> users = new Vector<>();
        String query = "SELECT * FROM users";
        connect.rs = connect.execQuery(query);

        try {
            while(connect.rs.next()) {
                String id = connect.rs.getString("id");  // Updated to 'id'
                String name = connect.rs.getString("name");  // Updated to 'name'
                String password = connect.rs.getString("password");
                String email = connect.rs.getString("email");
                String role = connect.rs.getString("role");
                users.add(new User(id, name, password, email, role));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    private String generateId() {
        String query = "SELECT id FROM users ORDER BY id DESC LIMIT 1";  // Updated to 'id'
        try {
            ResultSet rs = connect.execQuery(query);
            if (rs.next()) {
                String lastId = rs.getString("id");  // Updated to 'id'
                int num = Integer.parseInt(lastId.substring(1));
                return String.format("U%05d", num + 1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "U00001";
    }

    public String register(String name, String password, String email, String role) {
        String validationResult = checkRegisterInput(name, password, email, role);
        if (!validationResult.equals("Validation successful")) {
            return validationResult;
        }

        String generatedId = generateId();

        String query = "INSERT INTO users (id, name, password, email, role) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = connect.prepareStatement(query);
            ps.setString(1, generatedId); // id
            ps.setString(2, name);         // name
            ps.setString(3, password);     // password
            ps.setString(4, email);        // email
            ps.setString(5, role);         // role
            ps.executeUpdate();
            return "Registration successful";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Registration failed: Database error";
        }
    }

    // Modify other methods to align with the updated column names
    // 2. Change Profile
    public String changeProfile(String email, String name, String oldPassword, String newPassword) {
        if (!validatePassword(newPassword).isEmpty()) {
            return validatePassword(newPassword);
        }

        String query = "UPDATE users SET name = ?, password = ? WHERE email = ? AND password = ?";  // Updated 'username' to 'name'
        try {
            PreparedStatement ps = connect.prepareStatement(query);
            ps.setString(1, name);
            ps.setString(2, newPassword);
            ps.setString(3, email);
            ps.setString(4, oldPassword);
            int rowsUpdated = ps.executeUpdate();

            if (rowsUpdated > 0) {
                return "Profile updated successfully";
            } else {
                return "Update failed: Incorrect email or password";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Update failed: Database error";
        }
    }

    // 3. Get user by email
    public User getUserByEmail(String email) {
        String query = "SELECT * FROM users WHERE email = ?";
        try {
            PreparedStatement ps = connect.prepareStatement(query);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new User(
                    rs.getString("id"),       // Updated to 'id'
                    rs.getString("name"),     // Updated to 'name'
                    rs.getString("password"),
                    rs.getString("email"),
                    rs.getString("role")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 4. Get user by name
    public User getUserByName(String name) {   // Updated method name to match 'name' column
        String query = "SELECT * FROM users WHERE name = ?";  // Updated 'username' to 'name'
        try {
            PreparedStatement ps = connect.prepareStatement(query);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new User(
                    rs.getString("id"),       // Updated to 'id'
                    rs.getString("name"),     // Updated to 'name'
                    rs.getString("password"),
                    rs.getString("email"),
                    rs.getString("role")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 5. Check registration input
    public String checkRegisterInput(String name, String password, String email, String role) {
        if (!validateName(name).isEmpty()) {
            return validateName(name);
        }
        if (!validatePassword(password).isEmpty()) {
            return validatePassword(password);
        }
        if (!validateEmail(email).isEmpty()) {
            return validateEmail(email);
        }
        if (!validateRole(role).isEmpty()) {
            return validateRole(role);
        }
        return "Validation successful";
    }

    // Modify validation methods to align with the column names
    public String validateName(String name) {  // Updated 'username' to 'name'
        if (name == null || name.trim().isEmpty()) {
            return "Name is required";
        }
        if (name.trim().length() < 3) {
            return "Name must be at least 3 characters";
        }
        if (!isNameUnique(name)) {
            return "Name already taken";
        }
        return "";
    }

    private boolean isNameUnique(String name) {  // Updated 'username' to 'name'
        String query = "SELECT COUNT(*) FROM users WHERE name = ?";  // Updated 'username' to 'name'
        try (PreparedStatement ps = connect.prepareStatement(query)) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count == 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    // 6. Check change profile input
    public String checkChangeProfileInput(String email, String name, String oldPassword, String newPassword) {
        if (!validateEmail(email).isEmpty()) {
            return validateEmail(email);
        }
        if (name == null || name.trim().isEmpty()) {
            return "Username is required";
        }
        if (!validatePassword(newPassword).isEmpty()) {
            return validatePassword(newPassword);
        }
        if (oldPassword == null || oldPassword.trim().isEmpty()) {
            return "Old password is required";
        }
        return "Validation successful";
    }

    public User validateUserCredentials(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement ps = connect.prepareStatement(query)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String user_id = rs.getString("user_id");
                String userName = rs.getString("username");
                String pass = rs.getString("password");
                String email = rs.getString("email");
                String role = rs.getString("role");

                return new User(user_id, userName, pass, email, role);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String login(String username, String password) {
        if (username.equals("admin") && password.equals("admin")) {
            return "admin";
        }

        String usernameValidation = validateUsername(username);
        if (!usernameValidation.isEmpty()) {
            return usernameValidation;
        }

        String passwordValidation = validatePassword(password);
        if (!passwordValidation.isEmpty()) {
            return passwordValidation;
        }

        User validatedUser = validateUserCredentials(username, password);
        if (validatedUser != null) {
            return "Validation successful";
        } else {
            return "Invalid username or password. Please try again.";
        }
    }

    public String checkAccountValidation(String username, String password, String email, String role) {
        if (!validateUsername(username).equals("")) {
            return validateUsername(username);
        }
        if (!validatePassword(password).equals("")) {
            return validatePassword(password);
        }
        if (!validateEmail(email).equals("")) {
            return validateEmail(email);
        }
        if (!validateRole(role).equals("")) {
            return validateRole(role);
        }

        return "Validation successful";
    }

    public String validateUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return "Username is required";
        }
        if (username.trim().length() < 3) {
            return "Username must be at least 3 characters";
        }
        if (!isUsernameUnique(username)) {
            return "Username already taken";
        }
        return "";
    }
    
    private boolean isEmailUnique(String email) {
        String query = "SELECT COUNT(*) FROM users WHERE email = ?";
        try (PreparedStatement ps = connect.prepareStatement(query)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count == 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    private boolean isUsernameUnique(String username) {
        String query = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (PreparedStatement ps = connect.prepareStatement(query)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count == 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String validatePassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            return "Password is required";
        }
        if (password.trim().length() < 5) {
            return "Password must be at least 5 characters";
        }
        return "";
    }

    public String validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return "Email is required";
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            return "Invalid email format";
        }
        return "";
    }

    public String validateRole(String role) {
        if (role == null || role.trim().isEmpty()) {
            return "Role is required";
        }
        if (!role.equals("Event Organizer") && !role.equals("Vendor") && !role.equals("Guest")) {
            return "Role must be Event Organizer, Vendor, or Guest";
        }
        return "";
    }

    public String getUser_id() {
        return user_id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}
