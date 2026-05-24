package com.imdb.dao;

import com.imdb.model.Review;
import com.imdb.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAO {

    // ── ADD REVIEW ───────────────────────────────────────────────────────────
    public boolean addReview(Review review) {
        String sql = "INSERT INTO reviews (movie_id, user_id, review_text, rating) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt   (1, review.getMovieId());
            ps.setInt   (2, review.getUserId());
            ps.setString(3, review.getReviewText());
            ps.setInt   (4, review.getRating());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error adding review: " + e.getMessage());
            return false;
        }
    }

    // ── GET REVIEWS FOR A MOVIE ───────────────────────────────────────────────
    public List<Review> getReviewsByMovie(int movieId) {
        List<Review> list = new ArrayList<>();
        String sql = """
                SELECT r.*, u.username
                FROM reviews r
                JOIN users u ON r.user_id = u.user_id
                WHERE r.movie_id = ?
                ORDER BY r.created_at DESC
                """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, movieId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs, true));

        } catch (SQLException e) {
            System.out.println("Error fetching reviews: " + e.getMessage());
        }
        return list;
    }

    // ── GET REVIEWS BY USER ──────────────────────────────────────────────────
    public List<Review> getReviewsByUser(int userId) {
        List<Review> list = new ArrayList<>();
        String sql = """
                SELECT r.*, m.title AS movie_title, u.username
                FROM reviews r
                JOIN movies m ON r.movie_id = m.movie_id
                JOIN users  u ON r.user_id  = u.user_id
                WHERE r.user_id = ?
                ORDER BY r.created_at DESC
                """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Review rv = mapRow(rs, true);
                rv.setMovieTitle(rs.getString("movie_title"));
                list.add(rv);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching user reviews: " + e.getMessage());
        }
        return list;
    }

    // ── AVERAGE RATING FOR MOVIE ─────────────────────────────────────────────
    public double getAverageRating(int movieId) {
        String sql = "SELECT AVG(rating) FROM reviews WHERE movie_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, movieId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble(1);

        } catch (SQLException e) {
            System.out.println("Error calculating average: " + e.getMessage());
        }
        return 0.0;
    }

    // ── DELETE REVIEW ────────────────────────────────────────────────────────
    public boolean deleteReview(int reviewId) {
        String sql = "DELETE FROM reviews WHERE review_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, reviewId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error deleting review: " + e.getMessage());
            return false;
        }
    }

    // ── HELPER ───────────────────────────────────────────────────────────────
    private Review mapRow(ResultSet rs, boolean withUser) throws SQLException {
        Review r = new Review();
        r.setReviewId  (rs.getInt      ("review_id"));
        r.setMovieId   (rs.getInt      ("movie_id"));
        r.setUserId    (rs.getInt      ("user_id"));
        r.setReviewText(rs.getString   ("review_text"));
        r.setRating    (rs.getInt      ("rating"));
        r.setCreatedAt (rs.getTimestamp("created_at"));
        if (withUser) r.setUsername(rs.getString("username"));
        return r;
    }
}
