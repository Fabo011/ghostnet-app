package com.shea.shepherd.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ghostnet")
public class GhostNet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String location; // GPS coordinates

    @Column(nullable = false)
    private String size; // Estimated size

    @Enumerated(EnumType.STRING)
    private GhostNetStatus status; // Report status (Reported, In Process, etc.)

    @ManyToOne
    private AppUser reporter; // The person who reported the ghost net

    @ManyToOne
    private AppUser recoveryAgent; // The person assigned to recover the ghost net

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public GhostNetStatus getStatus() {
        return status;
    }

    public void setStatus(GhostNetStatus status) {
        this.status = status;
    }

    public AppUser getReporter() {
        return reporter;
    }

    public void setReporter(AppUser reporter) {
        this.reporter = reporter;
    }

    public AppUser getRecoveryAgent() {
        return recoveryAgent;
    }

    public void setRecoveryAgent(AppUser recoveryAgent) {
        this.recoveryAgent = recoveryAgent;
    }
}
