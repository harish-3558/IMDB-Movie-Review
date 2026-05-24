# IMDB Movie Rating & Review — Console App (Java + JDBC)

## Project Structure
```
imdb-movie-review/
├── src/com/imdb/
│   ├── util/
│   │   └── DBConnection.java      ← DB connection
│   ├── model/
│   │   ├── Movie.java
│   │   ├── User.java
│   │   └── Review.java
│   ├── dao/
│   │   ├── MovieDAO.java          ← DB queries for movies
│   │   ├── UserDAO.java           ← DB queries for users
│   │   └── ReviewDAO.java         ← DB queries for reviews
│   ├── service/
│   │   ├── MovieService.java      ← Business logic
│   │   ├── UserService.java
│   │   └── ReviewService.java
│   └── main/
│       └── Main.java              ← Entry point & menus
└── schema.sql                     ← Database setup
```

## Setup Steps

### 1. MySQL Setup
```bash
mysql -u root -p < schema.sql
```

### 2. Update DB credentials
`DBConnection.java`:
```java
private static final String URL      = "jdbc:mysql://localhost:3306/imdb_db";
private static final String USER     = "root";
private static final String PASSWORD = "333";
```

### 3. Add MySQL JDBC Driver
Download `mysql-connector-j-8.x.x.jar` from https://dev.mysql.com/downloads/connector/j/

### 4. Compile
```bash
javac -cp ".;mysql-connector-j-8.x.x.jar" src/com/imdb/**/*.java src/com/imdb/main/Main.java -d out/
```

### 5. Run
```bash
java -cp "out;mysql-connector-j-8.x.x.jar" com.imdb.main.Main
```

## Features
- Register & Login
- Browse all movies / Search by title or genre
- View movie details with average rating
- Submit a review with rating (1–10)
- View your own reviews
- Top 10 rated movies
- Add new movies
