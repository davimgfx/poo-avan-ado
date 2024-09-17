package cms;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ContentHSQLDB implements Persistence<Content> {

    // Database connection details
    private static final String DB_URL = "jdbc:hsqldb:mem:.";
    private static final String USER = "sa";
    private static final String PASSWORD = "";
    private Connection connection = null;

    // Constructor
    public ContentHSQLDB() {
        createTable();
    }

    // Method to get the database connection
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        }
        return connection;
    }

    // Method to create the "Content" table if it doesn't exist
    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS Content (" +
                     "id VARCHAR(255) PRIMARY KEY, " +
                     "text VARCHAR(10000))";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to save a content
    @Override
    public void save(Content content) {
        String sql = "INSERT INTO Content (id, text) VALUES (?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, content.getId());
            stmt.setString(2, content.getText());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to list all content
    @Override
    public List<Content> list() {
        List<Content> contents = new ArrayList<>();
        String sql = "SELECT * FROM Content";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Content content = new Content(rs.getString("id"), rs.getString("text"));
                contents.add(content);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return contents;
    }

    // Method to update a content
    @Override
    public void update(Content content) {
        String sql = "UPDATE Content SET text = ? WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, content.getText());
            stmt.setString(2, content.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to remove a content by its ID
    @Override
    public boolean remove(String id) {
        String sql = "DELETE FROM Content WHERE id = ?";
        boolean deleted = false;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            int rowsAffected = stmt.executeUpdate();
            deleted = rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return deleted;
    }
}
