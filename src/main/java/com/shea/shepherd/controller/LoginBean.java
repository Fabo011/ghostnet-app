package com.shea.shepherd.controller;

import com.shea.shepherd.model.AppUser;
import com.shea.shepherd.service.UserService;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class LoginBean implements Serializable {

    private String username;
    private String password;
    private String role;  // Role selected by the user (retriever or reporter)

    @Inject
    private UserService userService;

    private AppUser loggedInUser;

    public String login() {
        loggedInUser = userService.authenticate(username, password, role);

        if (loggedInUser != null) {
            // Successfully logged in or newly registered
            // Store userId and token in localStorage (you can handle this in JS on the frontend)
            return "reportGhostNet.xhtml?faces-redirect=true"; // Navigate to the next page
        }
        return "login.xhtml"; // Stay on login page if authentication fails
    }

    // Getters and setters for username, password, and role
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public AppUser getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(AppUser loggedInUser) {
        this.loggedInUser = loggedInUser;
    }
}

