package com.securex.fleetcore.controller;

import com.securex.fleetcore.entity.DispatchJob;
import com.securex.fleetcore.service.RouteOptimizationService;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/dispatch")
@Produces(MediaType.APPLICATION_JSON)
public class AssignmentController {

    @Inject
    private RouteOptimizationService routeOptimizer;

    @POST
    @Path("/optimize")
    public Response runDailyOptimization() {
        try {
            List<DispatchJob> generatedJobs = routeOptimizer.optimizeAndDispatch();
            
            if (generatedJobs.isEmpty()) {
                return Response.ok("No unassigned parcels or available vehicles found.").build();
            }
            
            return Response.ok(generatedJobs).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().entity(e.getMessage()).build();
        }
    }
}