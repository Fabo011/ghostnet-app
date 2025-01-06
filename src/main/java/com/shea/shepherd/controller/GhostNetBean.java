package com.shea.shepherd.controller;

import com.shea.shepherd.model.GhostNet;
import com.shea.shepherd.model.GhostNetStatus;
import com.shea.shepherd.service.GhostNetService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("uniqueGhostNetBean")
@RequestScoped
public class GhostNetBean {
    private GhostNet ghostNet = new GhostNet();

    @Inject
    private GhostNetService ghostNetService;

    public String reportGhostNet() {
        ghostNet.setStatus(GhostNetStatus.REPORTED);
        ghostNetService.saveGhostNet(ghostNet);
        return "success"; // Redirects to a "success.xhtml" page
    }

    public GhostNet getGhostNet() {
        return ghostNet;
    }

    public void setGhostNet(GhostNet ghostNet) {
        this.ghostNet = ghostNet;
    }
}


