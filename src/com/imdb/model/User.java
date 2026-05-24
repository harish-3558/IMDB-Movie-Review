package com.imdb.model;

public class User {

    private int    userId;
    private String username;
    private String email;
    private String password;

    public User() {}

    public User(String username, String email, String password) {
        this.username = username;
        this.email    = email;
        this.password = password;
    }

    // Getters & Setters
    public int    getUserId()              { return userId; }
    public void   setUserId(int id)        { this.userId = id; }

    public String getUsername()            { return username; }
    public void   setUsername(String u)    { this.username = u; }

    public String getEmail()               { return email; }
    public void   setEmail(String e)       { this.email = e; }

    public String getPassword()            { return password; }
    public void   setPassword(String p)    { this.password = p; }

    @Override
    public String toString() {
        return String.format("[%d] %s (%s)", userId, username, email);
    }
}
