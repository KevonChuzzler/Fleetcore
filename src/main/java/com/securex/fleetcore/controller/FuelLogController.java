package com.securex.fleetcore.controller;

import com.securex.fleetcore.entity.FuelLog;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@RequestScoped
@Path("/fuel")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FuelLogController {

    @PersistenceContext
    private EntityManager entityManager;

    @GET
    public List<FuelLog> getAllFuelLogs() {
        return entityManager.createQuery("SELECT f FROM FuelLog f", FuelLog.class).getResultList();
    }

    @POST
    @Transactional
    public Response logFuel(FuelLog fuelLog) {
        entityManager.persist(fuelLog);
        return Response.status(Response.Status.CREATED).entity(fuelLog).build();
    }

    @GET
    @Path("/economy/{vehicleId}")
    public Response getFuelEconomy(@PathParam("vehicleId") Long vehicleId) {
        // Fetch the two most recent fuel logs for this vehicle based on odometer
        List<FuelLog> logs = entityManager.createQuery(
            "SELECT f FROM FuelLog f WHERE f.vehicle.vehicleId = :vid ORDER BY f.odometerReading DESC", FuelLog.class)
            .setParameter("vid", vehicleId)
            .setMaxResults(2)
            .getResultList();

        if (logs.size() < 2) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\": \"Need at least two fuel logs to calculate economy\"}").build();
        }

        FuelLog latest = logs.get(0);
        FuelLog previous = logs.get(1);

        int distance = latest.getOdometerReading() - previous.getOdometerReading();
        
        if (distance <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\": \"Invalid odometer readings\"}").build();
        }

        // Calculate Liters per 100km
        double litersPer100Km = (latest.getLiters() / distance) * 100;

        // Return a formatted JSON response
        String result = String.format(
            "{\"vehicleId\": %d, \"litersPer100Km\": %.2f, \"distanceTraveledKm\": %d}",
            vehicleId, litersPer100Km, distance
        );

        return Response.ok(result).build();
    }
}