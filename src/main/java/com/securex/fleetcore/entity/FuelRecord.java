package com.securex.fleetcore.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDate;

@Entity
@Table(name = "FUEL_RECORD")
public class FuelRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fuel_id")
    private Long fuelId; // Stores Fuel ID[cite: 1]

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle; // Links to Vehicle ID[cite: 1]

    @NotNull
    @Column(name = "fuel_date")
    private LocalDate fuelDate; // Stores Fuel date[cite: 1]

    @NotNull
    @Positive
    @Column(name = "fuel_quantity")
    private Double fuelQuantity; // Stores Fuel quantity[cite: 1]

    @NotNull
    @PositiveOrZero
    @Column(name = "fuel_cost")
    private Double fuelCost; // Stores Fuel cost[cite: 1]

    @NotNull
    @PositiveOrZero
    @Column(name = "vehicle_mileage")
    private Integer vehicleMileage; // Stores Vehicle mileage[cite: 1]

    // Default constructor required by JPA
    public FuelRecord() {
    }

    // Getters and Setters
    public Long getFuelId() { return fuelId; }
    public void setFuelId(Long fuelId) { this.fuelId = fuelId; }

    public Vehicle getVehicle() { return vehicle; }
    public void setVehicle(Vehicle vehicle) { this.vehicle = vehicle; }

    public LocalDate getFuelDate() { return fuelDate; }
    public void setFuelDate(LocalDate fuelDate) { this.fuelDate = fuelDate; }

    public Double getFuelQuantity() { return fuelQuantity; }
    public void setFuelQuantity(Double fuelQuantity) { this.fuelQuantity = fuelQuantity; }

    public Double getFuelCost() { return fuelCost; }
    public void setFuelCost(Double fuelCost) { this.fuelCost = fuelCost; }

    public Integer getVehicleMileage() { return vehicleMileage; }
    public void setVehicleMileage(Integer vehicleMileage) { this.vehicleMileage = vehicleMileage; }
}