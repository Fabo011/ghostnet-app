package com.shea.shepherd.controller;

//import com.shea.shepherd.model.GhostNet;
//import com.shea.shepherd.model.GhostNetStatus;
//import com.shea.shepherd.service.GhostNetService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Alternative;
//import jakarta.inject.Inject;
import jakarta.inject.Named;

@Alternative
@Named("uniqueGhostNetBean")
@RequestScoped
public class GhostNetBean {
    public String getMessage() {
        return "Hello, World!";
    }
}

