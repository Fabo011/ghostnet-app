package com.shea.shepherd.service;

import com.shea.shepherd.model.GhostNet;
import com.shea.shepherd.repository.GhostNetRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

//@ApplicationScoped
public class GhostNetService {

    //@Inject
    private GhostNetRepository ghostNetRepository;

    public void saveGhostNet(GhostNet ghostNet) {
        ghostNetRepository.save(ghostNet);
    }

    public List<GhostNet> getAllGhostNets() {
        return ghostNetRepository.findAll();
    }
}
