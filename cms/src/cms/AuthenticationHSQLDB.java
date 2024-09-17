package cms;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AuthenticationHSQLDB implements Authentication {

    private static final String DB_URL = "jdbc:hsqldb:mem:.";
    private static final String USER = "sa";
    private static final String PASSWORD = "";
    private Connection connection = null;

    public AuthenticationHSQLDB() {
        createUserTable();
    }

    private Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        }
        return connection;
    }

    private void createUserTable() {
        String sql = "CREATE TABLE IF NOT EXISTS User (" +
                     "username VARCHAR(255) PRIMARY KEY, " +
                     "password VARCHAR(255))";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean auth(User user) {
        String sql = "SELECT * FROM User WHERE username = ? AND password = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public void createUser(User user) {
        String sql = "INSERT INTO User (username, password) VALUES (?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<User> listUsers() {
        String sql = "SELECT * FROM User";
        List<User> users = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String username = rs.getString("username");
                String password = rs.getString("password");
                users.add(new User(username, password));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    public boolean updateUser(User user) {
        String sql = "UPDATE User SET password = ? WHERE username = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getPassword());
            stmt.setString(2, user.getUsername());
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteUser(String username) {
        String sql = "DELETE FROM User WHERE username = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean changePassword(User user, String newPassword) {
        user.setPassword(newPassword);
        return updateUser(user);
    }
}
