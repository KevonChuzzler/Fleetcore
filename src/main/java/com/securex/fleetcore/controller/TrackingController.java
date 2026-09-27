package com.securex.fleetcore.controller;

import com.securex.fleetcore.entity.TrackingEvent;
import com.securex.fleetcore.service.TrackerApiService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/tracking")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TrackingController {

    @Inject
    private TrackerApiService trackerApiService;

    // Define a simple DTO to accept the incoming JSON payload
    public static class GpsPingPayload {
        public Long vehicleId;
        public Double latitude;
        public Double longitude;
        public Double speedKmh;
    }

    @POST
    @Path("/ping")
    public Response logVehicleLocation(GpsPingPayload payload) {
        try {
            if (payload.vehicleId == null || payload.latitude == null || payload.longitude == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Vehicle ID, latitude, and longitude are required.")
                        .build();
            }

            TrackingEvent event = trackerApiService.processGpsPing(
                    payload.vehicleId, 
                    payload.latitude, 
                    payload.longitude, 
                    payload.speedKmh != null ? payload.speedKmh : 0.0
            );

            return Response.status(Response.Status.CREATED).entity(event).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().entity("Failed to process GPS ping.").build();
        }
    }
}