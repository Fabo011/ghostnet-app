package com.shea.shepherd.controller;

import com.shea.shepherd.model.GhostNetEntity;
import com.shea.shepherd.model.GhostNetStatus;
import com.shea.shepherd.model.UserEntity;
import com.shea.shepherd.service.DatabaseService;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import java.util.List;


@RequestScoped
@Named
public class RetrieverBean {

    @Inject
    private DatabaseService databaseService;

    private List<GhostNetEntity> ghostNets;

    @PostConstruct
    public void init() {
        ghostNets = databaseService.getAllGhostNetsSortedByStatus();
    }

    public List<GhostNetEntity> getGhostNets() {
        return ghostNets;
    }

    public void registerForRecovery(Long ghostNetId) {
            try {
                System.out.println("ghostNetId: " + ghostNetId);

                String username = getUsernameFromSession();
                UserEntity retriever = new UserEntity();
                retriever.setName(username);

                System.out.println("retriever: " + retriever);

                databaseService.assignRetrieverToGhostNet(ghostNetId, retriever);

                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "You have registered for recovery.", null));

                /**
                 *         gne1_0.id=?
                 * 13-Jan-2025 19:59:35.502 INFO [http-nio-8080-exec-10] com.sun.faces.renderkit.RenderKitUtils.renderUnhandledMessages WARNING: FacesMessage(s) have been enqueued, but may not have been displayed.
                 * sourceId=null[severity=(ERROR 2), summary=(Failed to register for recovery. Please try again.), detail=(Failed to register for recovery. Please try again.)]
                 * */

                // Refresh the list
                ghostNets = databaseService.getAllGhostNetsSortedByStatus();
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to register for recovery. Please try again.", null));
            }
    }

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

    private String getUsernameFromSession() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        return (String) session.getAttribute("username");
    }

}
