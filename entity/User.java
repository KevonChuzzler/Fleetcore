package com.securex.fleetcore.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "USER")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId; // Stores User ID[cite: 1]

    @NotNull
    @Column(name = "name")
    private String name; // Stores Name[cite: 1]

    @NotNull
    @Email
    @Column(name = "email", unique = true)
    private String email; // Stores Email[cite: 1]

    @NotNull
    @Column(name = "password")
    private String password; // Stores Password[cite: 1]

    @NotNull
    @Column(name = "role")
    private String role; // Stores Role[cite: 1]

    // Default constructor required by JPA
    public User() {
    }

    // Getters and Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}