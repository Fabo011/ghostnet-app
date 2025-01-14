package com.shea.shepherd.controller;

import com.shea.shepherd.model.UserEntity;
import com.shea.shepherd.service.DatabaseService;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import org.mindrot.jbcrypt.BCrypt;

import java.util.logging.Level;
import java.util.logging.Logger;

@Named
@RequestScoped
public class UserLoginBean {
    private static final Logger LOGGER = Logger.getLogger(UserLoginBean.class.getName());

    @Inject
    private DatabaseService databaseService;  // Injecting the service layer for user operations

    private String username;
    private String role;
    private String phoneNumber;
    private String password;

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

    // Method to login or register the user with password check and session creation
    public String login() {
        basicValidation();

        UserEntity user = databaseService.findUserByUsername(username);

        if (user != null) {
          boolean validPassword = passwordCheck(user.getPassword());

             if (!validPassword) {
               FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid password.", null));
               return null;
             }

          createSession(user.getName(), user.getRole());
          return returnPage(user.getRole());
        } else {
          return registerUser();
        }
    }

    // Method to logout the user
    public String logout() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);

        if (session != null) {
            session.invalidate(); // Invalidate the session
        }

        return "login.xhtml?faces-redirect=true"; // Redirect to the login page
    }

    // Method to check if the user is logged in
    public void checkLogin() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);

        if (session == null || session.getAttribute("username") == null) {
            try {
                facesContext.getExternalContext().redirect("login.xhtml?faces-redirect=true");
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error check user if logged in:", e.getMessage());
            }
        }
    }

    private String registerUser() {
        if ("retriever".equals(role) && (phoneNumber == null || phoneNumber.isEmpty())) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Phone number is required for retrievers.", null));
            return null;
        }

        databaseService.createUser(username, role, phoneNumber, password);
        createSession(username, role);

        return returnPage(role);
    }

    private String returnPage(String role) {
        if ("reporter".equals(role)) {
            return "reporter.xhtml?faces-redirect=true";  // Redirect to reporter page
        } else if ("retriever".equals(role)) {
            return "retriever.xhtml?faces-redirect=true";  // Redirect to retriever page
        }
        return "login.xhtml?faces-redirect=true";
    }

    private boolean passwordCheck(String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }

    private void basicValidation() {
        if (username == null || username.isEmpty() || role == null || role.isEmpty() || password == null || password.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please provide a valid username, role, and password.", null));
            FacesContext.getCurrentInstance().validationFailed();
        }
    }

    private void createSession(String username, String role) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
        session.setAttribute("username", username);
        session.setAttribute("role", role);
    }
}
