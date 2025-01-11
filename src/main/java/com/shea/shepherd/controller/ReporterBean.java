package com.shea.shepherd.controller;

import com.shea.shepherd.service.DatabaseService;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;

@Named
@RequestScoped
public class ReporterBean {

    @Inject
    private DatabaseService databaseService;

    private String location;  // GPS Coordinates
    private String size;      // Estimated size

    // Getters and Setters
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getUsername() {
        System.out.println("Username is null");
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        String username = (String) session.getAttribute("username");
        System.out.println("Username: " + username);
        return username;
    }

    public String reportGhostNet() {
        try {

            String username = getUsername();
            databaseService.saveGhostNetReport(username, location, size);

            // Show success message
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Ghost net reported successfully.", null));

            // Reset the form
            location = null;
            size = null;

            return null;
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to report ghost net. Please try again.", null));
            return null;
        }
    }
}

