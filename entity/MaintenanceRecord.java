package com.securex.fleetcore.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDate;

@Entity
@Table(name = "MAINTENANCE_RECORD")
public class MaintenanceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maintenance_id")
    private Long maintenanceId; // Stores Maintenance ID[cite: 1]

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle; // Links to Vehicle ID[cite: 1]

    @NotNull
    @Column(name = "service_date")
    private LocalDate serviceDate; // Stores Service date[cite: 1]

    @NotNull
    @Column(name = "maintenance_type")
    private String maintenanceType; // Stores Maintenance type[cite: 1]

    @PositiveOrZero
    @Column(name = "cost")
    private Double cost; // Stores Cost[cite: 1]

    @Column(name = "notes")
    private String notes; // Stores Notes[cite: 1]

    // Default constructor required by JPA
    public MaintenanceRecord() {
    }

    // Getters and Setters
    public Long getMaintenanceId() { return maintenanceId; }
    public void setMaintenanceId(Long maintenanceId) { this.maintenanceId = maintenanceId; }

    public Vehicle getVehicle() { return vehicle; }
    public void setVehicle(Vehicle vehicle) { this.vehicle = vehicle; }

    public LocalDate getServiceDate() { return serviceDate; }
    public void setServiceDate(LocalDate serviceDate) { this.serviceDate = serviceDate; }

    public String getMaintenanceType() { return maintenanceType; }
    public void setMaintenanceType(String maintenanceType) { this.maintenanceType = maintenanceType; }

    public Double getCost() { return cost; }
    public void setCost(Double cost) { this.cost = cost; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}