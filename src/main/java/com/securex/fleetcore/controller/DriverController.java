package com.securex.fleetcore.controller;

import com.securex.fleetcore.dao.DriverDAO;
import com.securex.fleetcore.entity.Driver;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/drivers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DriverController {

    @Inject
    private DriverDAO driverDAO;

    @GET
    public List<Driver> getAllDrivers() {
        return driverDAO.findAll(); // Retrieves driver profiles and assignment info[cite: 1]
    }

    @GET
    @Path("/{id}")
    public Response getDriverById(@PathParam("id") Long id) {
        Driver driver = driverDAO.findById(id);
        if (driver == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(driver).build();
    }

    @POST
    public Response createDriver(Driver driver) {
        driverDAO.create(driver); // Stores and manages driver information[cite: 1]
        return Response.status(Response.Status.CREATED).entity(driver).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateDriver(@PathParam("id") Long id, Driver updatedDriver) {
        Driver existingDriver = driverDAO.findById(id);
        if (existingDriver == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        updatedDriver.setDriverId(id);
        driverDAO.update(updatedDriver);
        return Response.ok(updatedDriver).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteDriver(@PathParam("id") Long id) {
        Driver existingDriver = driverDAO.findById(id);
        if (existingDriver == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        driverDAO.delete(id);
        return Response.noContent().build();
    }
}