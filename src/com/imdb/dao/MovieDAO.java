package com.imdb.dao;

import com.imdb.model.Movie;
import com.imdb.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovieDAO {

    // ── CREATE ──────────────────────────────────────────────────────────────
    public boolean addMovie(Movie movie) {
        String sql = "INSERT INTO movies (title, genre, director, release_year) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, movie.getTitle());
            ps.setString(2, movie.getGenre());
            ps.setString(3, movie.getDirector());
            ps.setInt   (4, movie.getReleaseYear());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error adding movie: " + e.getMessage());
            return false;
        }
    }

    // ── READ ALL ─────────────────────────────────────────────────────────────
    public List<Movie> getAllMovies() {
        List<Movie> list = new ArrayList<>();
        String sql = "SELECT * FROM movies ORDER BY title";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) list.add(mapRow(rs));

        } catch (SQLException e) {
            System.out.println("Error fetching movies: " + e.getMessage());
        }
        return list;
    }

    // ── READ BY ID ───────────────────────────────────────────────────────────
    public Movie getMovieById(int id) {
        String sql = "SELECT * FROM movies WHERE movie_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);

        } catch (SQLException e) {
            System.out.println("Error fetching movie: " + e.getMessage());
        }
        return null;
    }

    // ── SEARCH BY TITLE ──────────────────────────────────────────────────────
    public List<Movie> searchByTitle(String keyword) {
        List<Movie> list = new ArrayList<>();
        String sql = "SELECT * FROM movies WHERE title LIKE ? ORDER BY title";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));

        } catch (SQLException e) {
            System.out.println("Error searching movies: " + e.getMessage());
        }
        return list;
    }

    // ── SEARCH BY GENRE ──────────────────────────────────────────────────────
    public List<Movie> searchByGenre(String genre) {
        List<Movie> list = new ArrayList<>();
        String sql = "SELECT * FROM movies WHERE genre LIKE ? ORDER BY title";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + genre + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));

        } catch (SQLException e) {
            System.out.println("Error searching by genre: " + e.getMessage());
        }
        return list;
    }

    // ── TOP RATED ────────────────────────────────────────────────────────────
    public List<String> getTopRatedMovies(int limit) {
        List<String> list = new ArrayList<>();
        String sql = """
                SELECT m.title, m.release_year, ROUND(AVG(r.rating), 2) AS avg_rating,
                       COUNT(r.review_id) AS total_reviews
                FROM movies m
                JOIN reviews r ON m.movie_id = r.movie_id
                GROUP BY m.movie_id, m.title, m.release_year
                ORDER BY avg_rating DESC
                LIMIT ?
                """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(String.format("%-35s (%d)  ★ %.2f  [%d reviews]",
                        rs.getString("title"),
                        rs.getInt("release_year"),
                        rs.getDouble("avg_rating"),
                        rs.getInt("total_reviews")));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching top movies: " + e.getMessage());
        }
        return list;
    }

    // ── UPDATE ───────────────────────────────────────────────────────────────
    public boolean updateMovie(Movie movie) {
        String sql = "UPDATE movies SET title=?, genre=?, director=?, release_year=? WHERE movie_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, movie.getTitle());
            ps.setString(2, movie.getGenre());
            ps.setString(3, movie.getDirector());
            ps.setInt   (4, movie.getReleaseYear());
            ps.setInt   (5, movie.getMovieId());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error updating movie: " + e.getMessage());
            return false;
        }
    }

    // ── DELETE ───────────────────────────────────────────────────────────────
    public boolean deleteMovie(int id) {
        String sql = "DELETE FROM movies WHERE movie_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error deleting movie: " + e.getMessage());
            return false;
        }
    }

    // ── HELPER ───────────────────────────────────────────────────────────────
    private Movie mapRow(ResultSet rs) throws SQLException {
        Movie m = new Movie();
        m.setMovieId    (rs.getInt   ("movie_id"));
        m.setTitle      (rs.getString("title"));
        m.setGenre      (rs.getString("genre"));
        m.setDirector   (rs.getString("director"));
        m.setReleaseYear(rs.getInt   ("release_year"));
        return m;
    }
}
