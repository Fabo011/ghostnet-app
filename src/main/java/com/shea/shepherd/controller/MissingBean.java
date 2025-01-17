package com.shea.shepherd.controller;

import com.shea.shepherd.model.GhostNetEntity;
import com.shea.shepherd.service.DatabaseService;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;

import java.util.List;

/**
 * This bean is request-scoped and interacts with the database service
 * to perform actions like marking a net as missing.
 * Database entity: GhostNetEntity
 * Using GhostNetStatus enum
 */
@Named
@RequestScoped
public class MissingBean {

    @Inject
    private DatabaseService databaseService;
    private String username = getUsernameFromSession();
    private List<GhostNetEntity> ghostNets;

    /**
     * Getters and Setters
     */
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * PostConstruct is called after ghostnet is marked as missing to update the table
     */
    @PostConstruct
    public void init() {
        ghostNets = databaseService.getAllGhostNetsSortedByStatus();
    }

    public List<GhostNetEntity> getGhostNets() {
        return ghostNets;
    }

    /**
     * Function to mark ghostnet as missing in happy case
     * returns error message to user if something failed
     */
    public void markAsMissing(Long netId) {
        try {
            databaseService.assignMissingReporterToGhostNet(netId, username);

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Ghost net marked as missing successfully.", null));

            ghostNets = databaseService.getAllGhostNetsSortedByStatus();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to mark as missing. Please try again.", null));
        }
    }

    /**
     * Function to get username from session
     */
    private String getUsernameFromSession() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        return (String) session.getAttribute("username");
    }
}
