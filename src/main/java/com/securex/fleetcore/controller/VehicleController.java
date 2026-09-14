package com.securex.fleetcore.controller;

import com.securex.fleetcore.dao.VehicleDAO;
import com.securex.fleetcore.entity.Vehicle;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/vehicles")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VehicleController {

    @Inject
    private VehicleDAO vehicleDAO;

    @GET
    public List<Vehicle> getAllVehicles() {
        return vehicleDAO.findAll(); // Retrieves and returns all vehicle records[cite: 1]
    }

    @GET
    @Path("/{id}")
    public Response getVehicleById(@PathParam("id") Long id) {
        Vehicle vehicle = vehicleDAO.findById(id);
        if (vehicle == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(vehicle).build(); // Returns a specific vehicle's information and status[cite: 1]
    }

    @POST
    public Response createVehicle(Vehicle vehicle) {
        vehicleDAO.create(vehicle); // Adds a new vehicle to the database[cite: 1]
        return Response.status(Response.Status.CREATED).entity(vehicle).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateVehicle(@PathParam("id") Long id, Vehicle updatedVehicle) {
        Vehicle existingVehicle = vehicleDAO.findById(id);
        if (existingVehicle == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        updatedVehicle.setVehicleId(id);
        vehicleDAO.update(updatedVehicle); // Updates existing vehicle information[cite: 1]
        return Response.ok(updatedVehicle).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteVehicle(@PathParam("id") Long id) {
        Vehicle existingVehicle = vehicleDAO.findById(id);
        if (existingVehicle == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        vehicleDAO.delete(id); // Deactivates or removes a vehicle[cite: 1]
        return Response.noContent().build();
    }
}