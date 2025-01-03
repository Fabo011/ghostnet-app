package com.shea.shepherd.model;

import jakarta.persistence.*;

@Entity
@Table(name = "appUser")
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String phoneNumber; // Optional, only for reporters

    private String role;

    // Getters and Setters
}
