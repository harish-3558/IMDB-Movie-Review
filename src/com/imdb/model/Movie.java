package com.imdb.model;

public class Movie {

    private int    movieId;
    private String title;
    private String genre;
    private String director;
    private int    releaseYear;

    public Movie() {}

    public Movie(String title, String genre, String director, int releaseYear) {
        this.title       = title;
        this.genre       = genre;
        this.director    = director;
        this.releaseYear = releaseYear;
    }

    // Getters & Setters
    public int    getMovieId()            { return movieId; }
    public void   setMovieId(int id)      { this.movieId = id; }

    public String getTitle()              { return title; }
    public void   setTitle(String t)      { this.title = t; }

    public String getGenre()              { return genre; }
    public void   setGenre(String g)      { this.genre = g; }

    public String getDirector()           { return director; }
    public void   setDirector(String d)   { this.director = d; }

    public int    getReleaseYear()        { return releaseYear; }
    public void   setReleaseYear(int y)   { this.releaseYear = y; }

    @Override
    public String toString() {
        return String.format("[%d] %s (%d) | Genre: %s | Director: %s",
                movieId, title, releaseYear, genre, director);
    }
}
