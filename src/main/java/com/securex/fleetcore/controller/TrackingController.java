package com.securex.fleetcore.controller;

import com.securex.fleetcore.dao.TrackingEventDAO;
import com.securex.fleetcore.entity.TrackingEvent;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/tracking")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TrackingController {

    @Inject
    private TrackingEventDAO trackingDAO;

    @GET
    public List<TrackingEvent> getAllTrackingEvents() {
        return trackingDAO.findAll(); // Makes vehicle location and status information available to authorised users[cite: 1]
    }

    @POST
    public Response receiveTrackingEvent(TrackingEvent event) {
        trackingDAO.create(event); // Receives vehicle location and status information from the tracking company[cite: 1]
        return Response.status(Response.Status.CREATED).entity(event).build();
    }
}