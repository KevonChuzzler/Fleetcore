package com.securex.fleetcore.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "NOTIFICATION")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long notificationId; // Stores Notification ID[cite: 1]

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Links to User ID[cite: 1]

    @Column(name = "notification_type")
    private String notificationType; // Stores Notification type[cite: 1]

    @NotNull
    @Column(name = "message")
    private String message; // Stores Message[cite: 1]

    @NotNull
    @Column(name = "created_at")
    private LocalDateTime createdAt; // Stores Date/time[cite: 1]

    @Column(name = "is_read")
    private Boolean isRead; // Stores Read/unread status[cite: 1]

    // Default constructor required by JPA
    public Notification() {
    }

    // Getters and Setters
    public Long getNotificationId() { return notificationId; }
    public void setNotificationId(Long notificationId) { this.notificationId = notificationId; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getNotificationType() { return notificationType; }
    public void setNotificationType(String notificationType) { this.notificationType = notificationType; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Boolean getIsRead() { return isRead; }
    public void setIsRead(Boolean isRead) { this.isRead = isRead; }
}