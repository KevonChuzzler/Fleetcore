package com.securex.fleetcore.controller;

import com.securex.fleetcore.entity.Driver;
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

@Path("/drivers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DriverController {

    @PersistenceContext
    private EntityManager entityManager;

    @GET
    public List<Driver> getAllDrivers() {
        return entityManager.createQuery("SELECT d FROM Driver d", Driver.class).getResultList();
    }

    @POST
    @Transactional
    public Response addDriver(Driver driver) {
        entityManager.persist(driver);
        return Response.status(Response.Status.CREATED).entity(driver).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateDriver(@PathParam("id") Long id, Driver updatedDriver) {
        Driver existing = entityManager.find(Driver.class, id);
        if (existing == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Driver not found").build();
        }
        
        updatedDriver.setId(id);
        entityManager.merge(updatedDriver);
        
        return Response.ok(updatedDriver).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteDriver(@PathParam("id") Long id) {
        Driver existing = entityManager.find(Driver.class, id);
        if (existing != null) {
            entityManager.remove(existing);
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).entity("Driver not found").build();
    }
}