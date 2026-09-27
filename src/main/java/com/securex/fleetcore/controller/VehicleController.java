package com.securex.fleetcore.controller;

import com.securex.fleetcore.entity.Driver;
import com.securex.fleetcore.entity.Vehicle;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/vehicles")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VehicleController {

    @PersistenceContext
    private EntityManager entityManager;

    @GET
    public List<Vehicle> getAllVehicles() {
        return entityManager.createQuery("SELECT v FROM Vehicle v", Vehicle.class).getResultList();
    }

    @POST
    @Transactional
    public Response addVehicle(Vehicle vehicle) {
        entityManager.persist(vehicle);
        return Response.status(Response.Status.CREATED).entity(vehicle).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateVehicle(@PathParam("id") Long id, Vehicle updatedVehicle) {
        Vehicle existing = entityManager.find(Vehicle.class, id);
        if (existing == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Vehicle not found").build();
        }
        
        updatedVehicle.setId(id);
        entityManager.merge(updatedVehicle);
        
        return Response.ok(updatedVehicle).build();
    }

    // New Endpoint: Assigns a Driver to a Vehicle
    @PUT
    @Path("/{id}/driver/{driverId}")
    @Transactional
    public Response assignDriver(@PathParam("id") Long vehicleId, @PathParam("driverId") Long driverId) {
        Vehicle vehicle = entityManager.find(Vehicle.class, vehicleId);
        Driver driver = entityManager.find(Driver.class, driverId);
        
        if (vehicle == null || driver == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Vehicle or Driver not found").build();
        }
        
        vehicle.setDriver(driver);
        entityManager.merge(vehicle);
        return Response.ok(vehicle).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteVehicle(@PathParam("id") Long id) {
        Vehicle existing = entityManager.find(Vehicle.class, id);
        if (existing != null) {
            entityManager.remove(existing);
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).entity("Vehicle not found").build();
    }
}