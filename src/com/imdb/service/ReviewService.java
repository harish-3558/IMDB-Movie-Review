package com.imdb.service;

import com.imdb.dao.ReviewDAO;
import com.imdb.model.Review;

import java.util.List;

public class ReviewService {

    private final ReviewDAO reviewDAO = new ReviewDAO();

    public boolean addReview(int movieId, int userId, String text, int rating) {
        if (rating < 1 || rating > 10) {
            System.out.println("  Rating must be between 1 and 10.");
            return false;
        }
        if (text == null || text.isBlank()) {
            System.out.println("  Review text cannot be empty.");
            return false;
        }
        return reviewDAO.addReview(new Review(movieId, userId, text.trim(), rating));
    }

    public List<Review> getReviewsForMovie(int movieId) {
        return reviewDAO.getReviewsByMovie(movieId);
    }

    public List<Review> getMyReviews(int userId) {
        return reviewDAO.getReviewsByUser(userId);
    }

    public boolean deleteReview(int reviewId) {
        return reviewDAO.deleteReview(reviewId);
    }

    public double getAverage(int movieId) {
        return reviewDAO.getAverageRating(movieId);
    }
}
