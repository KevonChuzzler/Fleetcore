package com.securex.fleetcore.dto;

public class VehicleAnalyticsDto {
    private Long vehicleId;
    private String registrationNumber;
    private String make;
    private String model;
    
    private Double totalFuelCost;
    private Double totalFuelLiters;
    private Double totalTripExpenses;
    private Double totalMaintenanceCost;
    private Double grandTotalCost;

    // --- Getters and Setters ---

    public Long getVehicleId() { return vehicleId; }
    public void setVehicleId(Long vehicleId) { this.vehicleId = vehicleId; }

    public String getRegistrationNumber() { return registrationNumber; }
    public void setRegistrationNumber(String registrationNumber) { this.registrationNumber = registrationNumber; }

    public String getMake() { return make; }
    public void setMake(String make) { this.make = make; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public Double getTotalFuelCost() { return totalFuelCost; }
    public void setTotalFuelCost(Double totalFuelCost) { this.totalFuelCost = totalFuelCost; }

    public Double getTotalFuelLiters() { return totalFuelLiters; }
    public void setTotalFuelLiters(Double totalFuelLiters) { this.totalFuelLiters = totalFuelLiters; }

    public Double getTotalTripExpenses() { return totalTripExpenses; }
    public void setTotalTripExpenses(Double totalTripExpenses) { this.totalTripExpenses = totalTripExpenses; }

    public Double getTotalMaintenanceCost() { return totalMaintenanceCost; }
    public void setTotalMaintenanceCost(Double totalMaintenanceCost) { this.totalMaintenanceCost = totalMaintenanceCost; }

    public Double getGrandTotalCost() { return grandTotalCost; }
    public void setGrandTotalCost(Double grandTotalCost) { this.grandTotalCost = grandTotalCost; }
}