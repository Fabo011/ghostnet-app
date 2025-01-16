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

@Named
@RequestScoped
public class MissingBean {

    @Inject
    private DatabaseService databaseService;
    private String username = getUsernameFromSession();
    private List<GhostNetEntity> ghostNets;

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @PostConstruct
    public void init() {
        ghostNets = databaseService.getAllGhostNetsSortedByStatus();
    }

    public List<GhostNetEntity> getGhostNets() {
        return ghostNets;
    }

    public String markAsMissing(Long netId) {
        try {
            System.out.println("Marked as missing netID: " + netId);
            System.out.println("Name: " + username);

            databaseService.assignMissingReporterToGhostNet(netId, username);

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Ghost net marked as missing.", null));

            // Refresh the list
            // TODO: Refresh of page with updated status was not working
            ghostNets = databaseService.getAllGhostNetsSortedByStatus();

            return null;
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to mark as missing. Please try again.", null));
            return null;
        }
    }

    private String getUsernameFromSession() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        return (String) session.getAttribute("username");
    }
}
