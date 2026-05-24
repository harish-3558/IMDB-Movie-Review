package com.imdb.service;

import com.imdb.dao.MovieDAO;
import com.imdb.dao.ReviewDAO;
import com.imdb.model.Movie;

import java.util.List;

public class MovieService {

    private final MovieDAO  movieDAO  = new MovieDAO();
    private final ReviewDAO reviewDAO = new ReviewDAO();

    public boolean addMovie(String title, String genre, String director, int year) {
        if (title == null || title.isBlank()) {
            System.out.println("  Title cannot be empty.");
            return false;
        }
        return movieDAO.addMovie(new Movie(title.trim(), genre, director, year));
    }

    public List<Movie> listAllMovies()              { return movieDAO.getAllMovies(); }
    public Movie       findById(int id)             { return movieDAO.getMovieById(id); }
    public List<Movie> searchByTitle(String kw)     { return movieDAO.searchByTitle(kw); }
    public List<Movie> searchByGenre(String genre)  { return movieDAO.searchByGenre(genre); }
    public boolean     deleteMovie(int id)          { return movieDAO.deleteMovie(id); }

    public List<String> topRated(int limit) {
        return movieDAO.getTopRatedMovies(limit);
    }

    public void showMovieDetails(int movieId) {
        Movie m = movieDAO.getMovieById(movieId);
        if (m == null) { System.out.println("  Movie not found."); return; }

        double avg = reviewDAO.getAverageRating(movieId);
        System.out.println("\n  " + "=".repeat(55));
        System.out.printf ("  Title    : %s%n", m.getTitle());
        System.out.printf ("  Genre    : %s%n", m.getGenre());
        System.out.printf ("  Director : %s%n", m.getDirector());
        System.out.printf ("  Year     : %d%n", m.getReleaseYear());
        System.out.printf ("  Avg Rating: %.2f / 10%n", avg);
        System.out.println("  " + "=".repeat(55));
    }

    public boolean updateMovie(int id, String title, String genre, String director, int year) {
        Movie m = new Movie(title, genre, director, year);
        m.setMovieId(id);
        return movieDAO.updateMovie(m);
    }
}
