package com.shea.shepherd.controller;

import com.shea.shepherd.model.GhostNetEntity;
import com.shea.shepherd.model.GhostNetStatus;
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
 * Users with role Retriever can add yourself to a ghostnet
 * Users with role Retriever can mark a ghostnet as recovered
 * Database entity: GhostNetEntity
 * Using GhostNetStatus enum
 */
@Named
@RequestScoped
public class RetrieverBean {

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
     * Function to register for recovery in happy case
     * returns error message to user if something failed
     */
    public void registerForRecovery(Long ghostNetId) {
            try {
                databaseService.assignRetrieverToGhostNet(ghostNetId, username);

                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "You have registered for recovery.", null));

                ghostNets = databaseService.getAllGhostNetsSortedByStatus();
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to register for recovery. Please try again.", null));
            }
    }

    /**
     * Function to mark a ghostnet as recovered
     * returns error message to user if something failed
     */
    public void markAsRecovered(Long ghostNetId) {
            try {
                databaseService.updateGhostNetStatus(ghostNetId, GhostNetStatus.RECOVERED);

                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Ghost net marked as recovered.", null));

                // Refresh the list
                ghostNets = databaseService.getAllGhostNetsSortedByStatus();
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to mark as recovered. Please try again.", null));
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
