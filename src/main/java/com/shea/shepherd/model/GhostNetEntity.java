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

    @Enumerated(EnumType.STRING)
    private GhostNetStatus status;   // Use Enum for status

    @ManyToOne
    @JoinColumn(name = "assigned_user_id")
    private UserEntity assignedUser;

    // Getters, Setters, Constructors
    public GhostNetStatus getStatus() {
        return status;
    }

    public void setStatus(GhostNetStatus status) {
        this.status = status;
    }
}
