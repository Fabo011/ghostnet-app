package com.shea.shepherd.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "appuser")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String phoneNumber; // Nullable for retrievers
    private String role;
    private String password;  // Add password field

    @OneToMany(mappedBy = "assignedUser")
    private List<GhostNetEntity> assignedGhostNets = new ArrayList<>();

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<GhostNetEntity> getAssignedGhostNets() {
        return assignedGhostNets;
    }

    public void setAssignedGhostNets(List<GhostNetEntity> assignedGhostNets) {
        this.assignedGhostNets = assignedGhostNets;
    }
}