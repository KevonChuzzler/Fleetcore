package com.securex.fleetcore.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "fuel_log")
public class FuelLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fuel_id")
    private Long fuelId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "driver_id", nullable = false)
    private Driver driver;

    @NotNull
    @Column(name = "fillup_date")
    private LocalDate fillupDate;

    @NotNull
    private Double liters;

    @NotNull
    @Column(name = "total_cost")
    private BigDecimal totalCost;

    @Column(name = "odometer_reading")
    private Integer odometerReading;

    public FuelLog() {}

    // Getters and Setters
    public Long getFuelId() { return fuelId; }
    public void setFuelId(Long fuelId) { this.fuelId = fuelId; }

    public Vehicle getVehicle() { return vehicle; }
    public void setVehicle(Vehicle vehicle) { this.vehicle = vehicle; }

    public Driver getDriver() { return driver; }
    public void setDriver(Driver driver) { this.driver = driver; }

    public LocalDate getFillupDate() { return fillupDate; }
    public void setFillupDate(LocalDate fillupDate) { this.fillupDate = fillupDate; }

    public Double getLiters() { return liters; }
    public void setLiters(Double liters) { this.liters = liters; }

    public BigDecimal getTotalCost() { return totalCost; }
    public void setTotalCost(BigDecimal totalCost) { this.totalCost = totalCost; }

    public Integer getOdometerReading() { return odometerReading; }
    public void setOdometerReading(Integer odometerReading) { this.odometerReading = odometerReading; }
}