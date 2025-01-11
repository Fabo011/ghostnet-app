package com.shea.shepherd.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ghostnet")
public class GhostNetEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String location; // GPS coordinates
    private String size;     // Estimated size
    private String reporterUsername;

    @Enumerated(EnumType.STRING)
    private GhostNetStatus status;   // Use Enum for status

    @ManyToOne
    @JoinColumn(name = "assigned_user_id")
    private UserEntity assignedUser; // Assigned retriever


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

    public UserEntity getAssignedUser() {
        return assignedUser;
    }

    public void setAssignedUser(UserEntity assignedUser) {
        this.assignedUser = assignedUser;
    }

    public String getReporterUsername() {
        return reporterUsername;
    }

    public void setReporterUsername(String reporterUsername) {
        this.reporterUsername = reporterUsername;
    }
}

