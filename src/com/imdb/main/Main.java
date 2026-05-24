package com.imdb.main;

import com.imdb.model.Movie;
import com.imdb.model.Review;
import com.imdb.model.User;
import com.imdb.service.MovieService;
import com.imdb.service.ReviewService;
import com.imdb.service.UserService;

import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner       sc            = new Scanner(System.in);
    private static final UserService   userService   = new UserService();
    private static final MovieService  movieService  = new MovieService();
    private static final ReviewService reviewService = new ReviewService();

    private static User currentUser = null;   // logged-in user

    // ════════════════════════════════════════════════════════════════════════
    //  ENTRY POINT
    // ════════════════════════════════════════════════════════════════════════
    public static void main(String[] args) {
        banner();
        boolean running = true;
        while (running) {
            if (currentUser == null) {
                running = guestMenu();
            } else {
                running = mainMenu();
            }
        }
        System.out.println("\n  Goodbye! 🎬\n");
        sc.close();
    }

    // ════════════════════════════════════════════════════════════════════════
    //  GUEST MENU (not logged in)
    // ════════════════════════════════════════════════════════════════════════
    private static boolean guestMenu() {
        printDivider();
        System.out.println("  GUEST MENU");
        System.out.println("  1. Register");
        System.out.println("  2. Login");
        System.out.println("  3. Browse Movies");
        System.out.println("  0. Exit");
        printDivider();
        System.out.print("  Choice: ");
        switch (input()) {
            case "1" -> doRegister();
            case "2" -> doLogin();
            case "3" -> browseMoviesMenu();
            case "0" -> { return false; }
            default  -> System.out.println("  Invalid option.");
        }
        return true;
    }

    // ════════════════════════════════════════════════════════════════════════
    //  MAIN MENU (logged in)
    // ════════════════════════════════════════════════════════════════════════
    private static boolean mainMenu() {
        printDivider();
        System.out.printf("  Logged in as: %s%n", currentUser.getUsername());
        System.out.println("  MAIN MENU");
        System.out.println("  1. Browse / Search Movies");
        System.out.println("  2. Add a Review");
        System.out.println("  3. View My Reviews");
        System.out.println("  4. Add New Movie  (admin)");
        System.out.println("  5. Top 10 Rated Movies");
        System.out.println("  6. Logout");
        System.out.println("  0. Exit");
        printDivider();
        System.out.print("  Choice: ");
        switch (input()) {
            case "1" -> browseMoviesMenu();
            case "2" -> doAddReview();
            case "3" -> doViewMyReviews();
            case "4" -> doAddMovie();
            case "5" -> doTopRated();
            case "6" -> { currentUser = null; System.out.println("  Logged out."); }
            case "0" -> { return false; }
            default  -> System.out.println("  Invalid option.");
        }
        return true;
    }

    // ════════════════════════════════════════════════════════════════════════
    //  AUTH
    // ════════════════════════════════════════════════════════════════════════
    private static void doRegister() {
        System.out.println("\n  -- REGISTER --");
        System.out.print("  Username : "); String u = input();
        System.out.print("  Email    : "); String e = input();
        System.out.print("  Password : "); String p = input();

        if (userService.register(u, e, p))
            System.out.println("  Registered successfully! Please login.");
        else
            System.out.println("  Registration failed.");
    }

    private static void doLogin() {
        System.out.println("\n  -- LOGIN --");
        System.out.print("  Username : "); String u = input();
        System.out.print("  Password : "); String p = input();

        User user = userService.login(u, p);
        if (user != null) {
            currentUser = user;
            System.out.println("  Welcome, " + user.getUsername() + "!");
        } else {
            System.out.println("  Invalid credentials.");
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    //  MOVIES
    // ════════════════════════════════════════════════════════════════════════
    private static void browseMoviesMenu() {
        printDivider();
        System.out.println("  BROWSE MOVIES");
        System.out.println("  1. All Movies");
        System.out.println("  2. Search by Title");
        System.out.println("  3. Search by Genre");
        System.out.println("  4. View Movie Details & Reviews");
        System.out.println("  0. Back");
        printDivider();
        System.out.print("  Choice: ");
        switch (input()) {
            case "1" -> listMovies(movieService.listAllMovies());
            case "2" -> {
                System.out.print("  Title keyword: ");
                listMovies(movieService.searchByTitle(input()));
            }
            case "3" -> {
                System.out.print("  Genre: ");
                listMovies(movieService.searchByGenre(input()));
            }
            case "4" -> doViewMovieDetails();
            case "0" -> {}
            default  -> System.out.println("  Invalid option.");
        }
    }

    private static void listMovies(List<Movie> movies) {
        System.out.println();
        if (movies.isEmpty()) { System.out.println("  No movies found."); return; }
        movies.forEach(m -> System.out.println("  " + m));
    }

    private static void doAddMovie() {
        System.out.println("\n  -- ADD MOVIE --");
        System.out.print("  Title       : "); String title    = input();
        System.out.print("  Genre       : "); String genre    = input();
        System.out.print("  Director    : "); String director = input();
        System.out.print("  Release Year: "); int    year     = intInput();

        if (movieService.addMovie(title, genre, director, year))
            System.out.println("  Movie added successfully!");
        else
            System.out.println("  Failed to add movie.");
    }

    private static void doViewMovieDetails() {
        System.out.print("  Enter Movie ID: ");
        int id = intInput();
        movieService.showMovieDetails(id);

        System.out.println("\n  -- REVIEWS --");
        List<Review> reviews = reviewService.getReviewsForMovie(id);
        if (reviews.isEmpty()) {
            System.out.println("  No reviews yet. Be the first!");
        } else {
            reviews.forEach(r -> System.out.println("  " + r));
        }
    }

    private static void doTopRated() {
        System.out.println("\n  -- TOP 10 RATED MOVIES --");
        List<String> top = movieService.topRated(10);
        if (top.isEmpty()) { System.out.println("  Not enough data yet."); return; }
        int rank = 1;
        for (String line : top) System.out.printf("  %2d. %s%n", rank++, line);
    }

    // ════════════════════════════════════════════════════════════════════════
    //  REVIEWS
    // ════════════════════════════════════════════════════════════════════════
    private static void doAddReview() {
        if (currentUser == null) { System.out.println("  Please login first."); return; }

        System.out.println("\n  -- ADD REVIEW --");
        System.out.print("  Movie ID   : "); int    movieId = intInput();
        System.out.print("  Rating(1-10): "); int    rating  = intInput();
        System.out.print("  Your Review: "); String text    = input();

        if (reviewService.addReview(movieId, currentUser.getUserId(), text, rating))
            System.out.println("  Review submitted! Thank you.");
        else
            System.out.println("  Failed to submit review.");
    }

    private static void doViewMyReviews() {
        if (currentUser == null) return;
        System.out.println("\n  -- MY REVIEWS --");
        List<Review> reviews = reviewService.getMyReviews(currentUser.getUserId());
        if (reviews.isEmpty()) {
            System.out.println("  You haven't reviewed anything yet.");
            return;
        }
        for (Review r : reviews) {
            System.out.printf("  [#%d] %s%n", r.getReviewId(),
                    r.getMovieTitle() != null ? r.getMovieTitle() : "Movie #" + r.getMovieId());
            System.out.println("  " + r);
            System.out.println();
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    //  HELPERS
    // ════════════════════════════════════════════════════════════════════════
    private static String input()    { return sc.nextLine().trim(); }
    private static int    intInput() {
        try   { return Integer.parseInt(sc.nextLine().trim()); }
        catch (NumberFormatException e) { return -1; }
    }

    private static void banner() {
        System.out.println("\n  ╔══════════════════════════════════════╗");
        System.out.println("  ║     🎬  IMDB Movie Rating & Review     ║");
        System.out.println("  ╚══════════════════════════════════════╝");
    }

    private static void printDivider() {
        System.out.println("  " + "-".repeat(40));
    }
}
