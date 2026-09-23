package com.securex.fleetcore.controller;

import com.securex.fleetcore.entity.VehicleAssignment;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/assignments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class AssignmentController {

    @PersistenceContext(unitName = "FleetcorePU")
    private EntityManager em;

    @GET
    public List<VehicleAssignment> getAllAssignments() {
        return em.createQuery("SELECT a FROM VehicleAssignment a", VehicleAssignment.class).getResultList();
    }

    @POST
    @Transactional
    public Response createAssignment(VehicleAssignment assignment) {
        try {
            em.persist(assignment);
            return Response.status(Response.Status.CREATED).entity(assignment).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"Failed to create assignment: " + e.getMessage() + "\"}")
                    .build();
        }
    }
}