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


/**
 * UserLoginBean is a bean class that handles user login and registration.
 * It uses the DatabaseService to perform user operations.
 * Database entity: UserEntity
 * User Roles: reporter, retriever, missingreporter
 */
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

    /**
     * Getters and Setters
     */
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

    /**
     * Method to login a user
     * Checks if user already registered, if not then registers the user
     * If a user already registered then checks the password and login the user
     * Password are hashed
     * Creates a session for the user to check if the user is logged in or not in other pages which are protected
     */
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

    /**
     * Method to logout a user
     * Invalidates the session and redirects to the login page
     */
    public String logout() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);

        if (session != null) {
            session.invalidate(); // Invalidate the session
        }

        return "login.xhtml?faces-redirect=true"; // Redirect to the login page
    }

    /**
     * Method to check if a user is logged in with session
     * If user is not logged in then redirects to the login page
     */
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

    /**
     * Method to register a user, is used in login method
     */
    private String registerUser() {
        if ("retriever".equals(role) && (phoneNumber == null || phoneNumber.isEmpty())) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Phone number is required for retrievers.", null));
            return null;
        }

        if ("missingreporter".equals(role) && (phoneNumber == null || phoneNumber.isEmpty())) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Phone number is required for missing reporters.", null));
            return null;
        }

        databaseService.createUser(username, role, phoneNumber, password);
        createSession(username, role);

        return returnPage(role);
    }

    /**
     * Method to return the page based on the user role
     * Is used in login and register method
     */
    private String returnPage(String role) {
        if ("reporter".equals(role)) {
            return "reporter.xhtml?faces-redirect=true";
        } else if ("retriever".equals(role)) {
            return "retriever.xhtml?faces-redirect=true";
        } else if ("missingreporter".equals(role)) {
            return "missing.xhtml?faces-redirect=true";
        }
        return "login.xhtml?faces-redirect=true";
    }

    /**
     * Method to check if the password is correct
     * Is used in login method
     */
    private boolean passwordCheck(String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }

    /**
     * Method to do basic validation
     * Is used in login and register method
     */
    private void basicValidation() {
        if (username == null || username.isEmpty() || role == null || role.isEmpty() || password == null || password.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please provide a valid username, role, and password.", null));
            FacesContext.getCurrentInstance().validationFailed();
        }
    }

    /**
     * Method to create a session for the user
     * Is used in login and register method
     */
    private void createSession(String username, String role) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
        session.setAttribute("username", username);
        session.setAttribute("role", role);
    }
}
