package com.securex.fleetcore.service;

import com.securex.fleetcore.entity.TrackingEvent;
import com.securex.fleetcore.entity.Vehicle;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;

@ApplicationScoped
public class TrackerApiService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void logEvent(Long vehicleId, Double latitude, Double longitude, Double speed) {
        Vehicle vehicle = entityManager.find(Vehicle.class, vehicleId);
        if (vehicle == null) {
            System.err.println("TrackerApiService: Vehicle " + vehicleId + " not found.");
            return;
        }

        TrackingEvent event = new TrackingEvent();
        event.setVehicle(vehicle);
        event.setLatitude(latitude);
        event.setLongitude(longitude);
        
        // Fixed: Changed from setSpeedKmh to setSpeed to match the TrackingEvent entity
        event.setSpeed(speed);
        
        event.setTimestamp(LocalDateTime.now());

        entityManager.persist(event);
    }
}