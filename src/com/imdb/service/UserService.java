package com.imdb.service;

import com.imdb.dao.UserDAO;
import com.imdb.model.User;

public class UserService {

    private final UserDAO userDAO = new UserDAO();

    public boolean register(String username, String email, String password) {
        if (username.isBlank() || email.isBlank() || password.isBlank()) {
            System.out.println("  All fields are required.");
            return false;
        }
        if (!email.contains("@")) {
            System.out.println("  Invalid email format.");
            return false;
        }
        if (password.length() < 4) {
            System.out.println("  Password must be at least 4 characters.");
            return false;
        }
        return userDAO.register(new User(username.trim(), email.trim(), password));
    }

    public User login(String username, String password) {
        return userDAO.login(username, password);
    }
}
