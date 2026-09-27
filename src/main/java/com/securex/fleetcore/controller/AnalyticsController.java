package com.securex.fleetcore.controller;

import com.securex.fleetcore.dto.VehicleAnalyticsDto;
import com.securex.fleetcore.entity.Vehicle;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.NoResultException;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/analytics")
@Produces(MediaType.APPLICATION_JSON)
public class AnalyticsController {

    @PersistenceContext
    private EntityManager entityManager;

    @GET
    @Path("/vehicle/{id}")
    public Response getVehicleAnalytics(@PathParam("id") Long vehicleId) {
        Vehicle vehicle = entityManager.find(Vehicle.class, vehicleId);
        if (vehicle == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Vehicle not found.").build();
        }

        // 1. Calculate Total Fuel Cost and Liters
        Double fuelCost = 0.0;
        Double fuelLiters = 0.0;
        try {
            Double sumCost = entityManager.createQuery(
                    "SELECT SUM(f.totalCost) FROM FuelLog f WHERE f.vehicle.id = :vid", Double.class)
                    .setParameter("vid", vehicleId)
                    .getSingleResult();
            if (sumCost != null) fuelCost = sumCost;

            Double sumLiters = entityManager.createQuery(
                    "SELECT SUM(f.liters) FROM FuelLog f WHERE f.vehicle.id = :vid", Double.class)
                    .setParameter("vid", vehicleId)
                    .getSingleResult();
            if (sumLiters != null) fuelLiters = sumLiters;
        } catch (NoResultException ignored) {}

        // 2. Calculate Total Trip Expenses
        Double expenseCost = 0.0;
        try {
            Double sumExp = entityManager.createQuery(
                    "SELECT SUM(e.amount) FROM TripExpense e WHERE e.vehicle.id = :vid", Double.class)
                    .setParameter("vid", vehicleId)
                    .getSingleResult();
            if (sumExp != null) expenseCost = sumExp;
        } catch (NoResultException ignored) {}

        // 3. Calculate Total Maintenance Cost
        Double maintenanceCost = 0.0;
        try {
            Double sumMaint = entityManager.createQuery(
                    "SELECT SUM(m.cost) FROM MaintenanceRecord m WHERE m.vehicle.id = :vid", Double.class)
                    .setParameter("vid", vehicleId)
                    .getSingleResult();
            if (sumMaint != null) maintenanceCost = sumMaint;
        } catch (NoResultException ignored) {}

        Double grandTotal = fuelCost + expenseCost + maintenanceCost;

        // 4. Build and return DTO
        VehicleAnalyticsDto dto = new VehicleAnalyticsDto();
        dto.setVehicleId(vehicleId);
        dto.setRegistrationNumber(vehicle.getRegistrationNumber());
        dto.setMake(vehicle.getMake());
        dto.setModel(vehicle.getModel());
        dto.setTotalFuelCost(fuelCost);
        dto.setTotalFuelLiters(fuelLiters);
        dto.setTotalTripExpenses(expenseCost);
        dto.setTotalMaintenanceCost(maintenanceCost);
        dto.setGrandTotalCost(grandTotal);

        return Response.ok(dto).build();
    }
}