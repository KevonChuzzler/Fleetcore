package com.securex.fleetcore.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "VEHICLE")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vehicle_id")
    private Long vehicleId; // Stores Vehicle ID[cite: 1]

    @NotNull
    @Size(min = 2, max = 20)
    @Column(name = "registration_number", unique = true)
    private String registrationNumber; // Stores Registration number[cite: 1]

    @Column(name = "make")
    private String make; // Stores Make[cite: 1]

    @Column(name = "model")
    private String model; // Stores Model[cite: 1]

    @Column(name = "vehicle_type")
    private String vehicleType; // Stores Vehicle type[cite: 1]

    @Column(name = "year")
    private Integer year; // Stores Year[cite: 1]

    @PositiveOrZero
    @Column(name = "mileage")
    private Integer mileage; // Stores Mileage[cite: 1]

    @Column(name = "status")
    private String status; // Stores Status[cite: 1]

    // Default constructor required by JPA
    public Vehicle() {
    }

    // Getters and Setters
    public Long getVehicleId() { return vehicleId; }
    public void setVehicleId(Long vehicleId) { this.vehicleId = vehicleId; }

    public String getRegistrationNumber() { return registrationNumber; }
    public void setRegistrationNumber(String registrationNumber) { this.registrationNumber = registrationNumber; }

    public String getMake() { return make; }
    public void setMake(String make) { this.make = make; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }

    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }

    public Integer getMileage() { return mileage; }
    public void setMileage(Integer mileage) { this.mileage = mileage; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}