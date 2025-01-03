package com.shea.shepherd.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ghostNet")
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
}
