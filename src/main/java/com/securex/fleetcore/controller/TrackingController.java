package com.securex.fleetcore.controller;

import com.securex.fleetcore.entity.TrackingEvent;
import com.securex.fleetcore.entity.Vehicle;
import com.securex.fleetcore.websocket.TelemetryWebSocket;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.Instant;
import java.util.Locale;

@Path("/tracking")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TrackingController {

    @PersistenceContext
    private EntityManager entityManager;

    public static class TrackingPayload {
        public Long vehicleId;
        public Double latitude;
        public Double longitude;
        public Double speed;
    }

    @POST
    @Transactional
    public Response logTrackingEvent(TrackingPayload payload) {
        Vehicle vehicle = entityManager.find(Vehicle.class, payload.vehicleId);
        if (vehicle == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Vehicle not found.").build();
        }

        // 1. Save to Database
        TrackingEvent event = new TrackingEvent();
        event.setVehicle(vehicle);
        event.setLatitude(payload.latitude);
        event.setLongitude(payload.longitude);
        event.setSpeed(payload.speed);
        
        entityManager.persist(event);

        // 2. Broadcast live to all connected WebSockets
        // FIX: Force Locale.US to ensure decimals use dots instead of commas for valid JSON
        String liveJson = String.format(Locale.US,
            "{\"vehicleId\": %d, \"registration\": \"%s\", \"lat\": %f, \"lng\": %f, \"speed\": %f, \"time\": \"%s\"}",
            vehicle.getId(),
            vehicle.getRegistrationNumber(),
            payload.latitude,
            payload.longitude,
            payload.speed,
            Instant.now().toString()
        );
        
        TelemetryWebSocket.broadcast(liveJson);

        return Response.status(Response.Status.CREATED).entity(event).build();
    }
}