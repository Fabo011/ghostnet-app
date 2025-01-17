package com.shea.shepherd.controller;

import com.shea.shepherd.service.DatabaseService;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;


/**
 * ReporterBean is a bean class that handles the reporting of ghost nets.
 * It uses the DatabaseService to save the report.
 * Database entity: GhstNetEntity
 * Using GhostNetStatus enum
 */
@Named
@RequestScoped
public class ReporterBean {

    @Inject
    private DatabaseService databaseService;

    private String location;  // GPS Coordinates
    private String size;      // Estimated size

    /**
     * Getters and Setters
     */
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

    /**
     * Function to get username from session
     */
    public String getUsername() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        return (String) session.getAttribute("username");
    }

    /**
     * Function to report ghostnet in happy case
     * if anything fails return FacesContext error message to user
     * Database Entity: GhostNetEntity
     * Database Enum: GhostNetStatus
     */
    public String reportGhostNet() {
        try {
            // Check if ghostnet with location x already exists, if yes return with FacesContext message
            if (databaseService.findGhostNetByLocation(location) != null) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ghost net already reported at this location.", null));
                return null;
            }

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

