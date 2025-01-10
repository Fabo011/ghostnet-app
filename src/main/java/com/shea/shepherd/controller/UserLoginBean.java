package com.shea.shepherd.controller;

import com.shea.shepherd.model.UserEntity;
import com.shea.shepherd.service.DatabaseService;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@RequestScoped
public class UserLoginBean {

    @Inject
    private DatabaseService databaseService;  // Injecting the service layer for user operations

    private String username;
    private String role;
    private String phoneNumber;
    private String password;
    private boolean isLoggedIn = false;

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    // Method to login or register the user
    public String login() {
        // Check that username, role, and password are provided
        if (username == null || username.isEmpty() || role == null || role.isEmpty() || password == null || password.isEmpty()) {
            System.out.println("Invalid input-------------------------");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please provide a valid username, role, and password.", null));
            FacesContext.getCurrentInstance().validationFailed();
            return null;  // Stay on the current page
        }

        // Check if the user already exists
        UserEntity user = databaseService.findUserByUsername(username);

        if (user != null) {
          return returnPage();
        } else {
          return registerUser();
        }
    }

    public String registerUser() {
        if ("retriever".equals(role) && (phoneNumber == null || phoneNumber.isEmpty())) {
            System.out.println("Retriever phone number required check-------------------------");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Phone number is required for retrievers.", null));
            return null;  // Stay on the current page
        }

        databaseService.createUser(username, role, phoneNumber, password);

        return returnPage();
    }

    public String returnPage() {
        if ("reporter".equals(role)) {
            isLoggedIn = true;
            System.out.println("Reporter LoggedIn Successfully-------------------------");
            return "reporter.xhtml?faces-redirect=true";  // Redirect to reporter page
        } else if ("retriever".equals(role)) {
            isLoggedIn = true;
            System.out.println("Retriever LoggedIn Successfully-------------------------");
            return "retriever.xhtml?faces-redirect=true";  // Redirect to retriever page
        }
        return "login.xhtml?faces-redirect=true";
    }
}




