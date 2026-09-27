package com.securex.fleetcore.service;

import com.securex.fleetcore.entity.TrackingEvent;
import com.securex.fleetcore.entity.Vehicle;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class TrackerApiService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public TrackingEvent processGpsPing(Long vehicleId, Double latitude, Double longitude, Double speedKmh) {
        Vehicle vehicle = entityManager.find(Vehicle.class, vehicleId);
        
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle not found with ID: " + vehicleId);
        }

        // 1. Create and save the historical tracking event
        TrackingEvent event = new TrackingEvent();
        event.setVehicle(vehicle);
        event.setLatitude(latitude);
        event.setLongitude(longitude);
        event.setSpeedKmh(speedKmh);
        
        entityManager.persist(event);

        // 2. Update the Vehicle's current state (assuming you want to track real-time status)
        // If your Vehicle entity has currentLat/currentLng fields, update them here:
        // vehicle.setCurrentLatitude(latitude);
        // vehicle.setCurrentLongitude(longitude);
        // vehicle.setStatus(speedKmh > 0 ? "EN_ROUTE" : "IDLE");
        // entityManager.merge(vehicle);

        return event;
    }
}