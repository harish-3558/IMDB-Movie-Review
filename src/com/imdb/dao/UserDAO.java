package com.imdb.dao;

import com.imdb.model.User;
import com.imdb.util.DBConnection;

import java.sql.*;

public class UserDAO {

    // ── REGISTER ─────────────────────────────────────────────────────────────
    public boolean register(User user) {
        String sql = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());   // hash in production!
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate")) {
                System.out.println("  Username or email already exists.");
            } else {
                System.out.println("Error registering user: " + e.getMessage());
            }
            return false;
        }
    }

    // ── LOGIN ────────────────────────────────────────────────────────────────
    public User login(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);

        } catch (SQLException e) {
            System.out.println("Error during login: " + e.getMessage());
        }
        return null;
    }

    // ── GET BY ID ────────────────────────────────────────────────────────────
    public User getUserById(int id) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);

        } catch (SQLException e) {
            System.out.println("Error fetching user: " + e.getMessage());
        }
        return null;
    }

    // ── HELPER ───────────────────────────────────────────────────────────────
    private User mapRow(ResultSet rs) throws SQLException {
        User u = new User();
        u.setUserId  (rs.getInt   ("user_id"));
        u.setUsername(rs.getString("username"));
        u.setEmail   (rs.getString("email"));
        u.setPassword(rs.getString("password"));
        return u;
    }
}
