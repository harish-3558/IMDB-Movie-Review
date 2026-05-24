
CREATE DATABASE IF NOT EXISTS imdb_db;
USE imdb_db;

CREATE TABLE IF NOT EXISTS movies (
    movie_id     INT          PRIMARY KEY AUTO_INCREMENT,
    title        VARCHAR(200) NOT NULL,
    genre        VARCHAR(100),
    director     VARCHAR(100),
    release_year INT
);

CREATE TABLE IF NOT EXISTS users (
    user_id  INT          PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50)  UNIQUE NOT NULL,
    email    VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS reviews (
    review_id   INT       PRIMARY KEY AUTO_INCREMENT,
    movie_id    INT       NOT NULL,
    user_id     INT       NOT NULL,
    review_text TEXT,
    rating      INT       NOT NULL CHECK (rating BETWEEN 1 AND 10),
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (movie_id) REFERENCES movies(movie_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id)  REFERENCES users(user_id)   ON DELETE CASCADE
);

INSERT INTO movies (title, genre, director, release_year) VALUES
('The Shawshank Redemption', 'Drama',     'Frank Darabont',   1994),
('The Dark Knight',          'Action',    'Christopher Nolan', 2008),
('Inception',                'Sci-Fi',    'Christopher Nolan', 2010),
('Interstellar',             'Sci-Fi',    'Christopher Nolan', 2014),
('Parasite',                 'Thriller',  'Bong Joon-ho',      2019);
