package com.securex.fleetcore.controller;

import com.securex.fleetcore.entity.FuelLog;
import com.securex.fleetcore.entity.Vehicle;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/fuel")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FuelLogController {

    @PersistenceContext
    private EntityManager entityManager;

    public static class FuelLogPayload {
        public Long vehicleId;
        public Double liters;
        public Double totalCost;
        public Double odometerKm;
    }

    @POST
    @Transactional
    public Response logFuel(FuelLogPayload payload) {
        Vehicle vehicle = entityManager.find(Vehicle.class, payload.vehicleId);
        if (vehicle == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Vehicle not found.").build();
        }

        FuelLog log = new FuelLog();
        log.setVehicle(vehicle);
        log.setLiters(payload.liters);
        log.setTotalCost(payload.totalCost);
        log.setOdometerKm(payload.odometerKm);

        entityManager.persist(log);
        return Response.status(Response.Status.CREATED).entity(log).build();
    }
}