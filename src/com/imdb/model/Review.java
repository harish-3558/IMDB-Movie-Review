package com.imdb.model;

import java.sql.Timestamp;

public class Review {

    private int       reviewId;
    private int       movieId;
    private int       userId;
    private String    reviewText;
    private int       rating;        // 1 – 10
    private Timestamp createdAt;

    // Extra display fields (from JOINs)
    private String    movieTitle;
    private String    username;

    public Review() {}

    public Review(int movieId, int userId, String reviewText, int rating) {
        this.movieId    = movieId;
        this.userId     = userId;
        this.reviewText = reviewText;
        this.rating     = rating;
    }

    // Getters & Setters
    public int       getReviewId()                 { return reviewId; }
    public void      setReviewId(int id)           { this.reviewId = id; }

    public int       getMovieId()                  { return movieId; }
    public void      setMovieId(int id)            { this.movieId = id; }

    public int       getUserId()                   { return userId; }
    public void      setUserId(int id)             { this.userId = id; }

    public String    getReviewText()               { return reviewText; }
    public void      setReviewText(String text)    { this.reviewText = text; }

    public int       getRating()                   { return rating; }
    public void      setRating(int r)              { this.rating = r; }

    public Timestamp getCreatedAt()                { return createdAt; }
    public void      setCreatedAt(Timestamp t)     { this.createdAt = t; }

    public String    getMovieTitle()               { return movieTitle; }
    public void      setMovieTitle(String t)       { this.movieTitle = t; }

    public String    getUsername()                 { return username; }
    public void      setUsername(String u)         { this.username = u; }

    @Override
    public String toString() {
        return String.format("  ★ %d/10  |  @%s  |  \"%s\"  [%s]",
                rating, username != null ? username : "user#" + userId,
                reviewText, createdAt);
    }
}
