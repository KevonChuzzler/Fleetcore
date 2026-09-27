package com.securex.fleetcore.service;

import com.securex.fleetcore.entity.DispatchJob;
import com.securex.fleetcore.entity.Parcel;
import com.securex.fleetcore.entity.Vehicle;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class RouteOptimizationService {

    @PersistenceContext
    private EntityManager entityManager;

    // Hardcoded depot coordinates (e.g., Nelspruit Warehouse)
    private static final double DEPOT_LAT = -25.4753;
    private static final double DEPOT_LNG = 30.9694;
    private static final double DEFAULT_VEHICLE_CAPACITY_KG = 1000.0; // 1 Ton fallback

    @Transactional
    public List<DispatchJob> optimizeAndDispatch() {
        // 1. Fetch unassigned parcels and available vehicles
        List<Parcel> unassignedParcels = entityManager.createQuery(
                "SELECT p FROM Parcel p WHERE p.dispatchJob IS NULL AND p.latitude IS NOT NULL", Parcel.class)
                .getResultList();

        List<Vehicle> availableVehicles = entityManager.createQuery(
                "SELECT v FROM Vehicle v", Vehicle.class) // Simplified: grab all vehicles
                .getResultList();

        List<DispatchJob> createdJobs = new ArrayList<>();

        // 2. Load Balancing (Greedy Bin Packing)
        for (Vehicle vehicle : availableVehicles) {
            if (unassignedParcels.isEmpty()) break;

            DispatchJob job = new DispatchJob();
            job.setVehicle(vehicle);
            job.setStatus("SCHEDULED");
            
            entityManager.persist(job);
            entityManager.flush(); // Forces the insert and ID generation immediately to prevent constraint errors

            double currentLoad = 0.0;
            List<Parcel> assignedToVehicle = new ArrayList<>();

            // Assign parcels until capacity is reached
            for (int i = 0; i < unassignedParcels.size(); i++) {
                Parcel p = unassignedParcels.get(i);
                double weight = p.getWeightKg() != null ? p.getWeightKg() : 0.0;

                if (currentLoad + weight <= DEFAULT_VEHICLE_CAPACITY_KG) {
                    currentLoad += weight;
                    p.setDispatchJob(job);
                    assignedToVehicle.add(p);
                    unassignedParcels.remove(i);
                    i--; // Adjust index after removal
                }
            }

            // 3. Nearest Neighbor Routing
            List<Parcel> optimizedRoute = calculateNearestNeighborRoute(assignedToVehicle);
            
            // Assign the sorted parcels to the job
            job.setParcels(optimizedRoute);
            createdJobs.add(job);
        }

        return createdJobs;
    }

    private List<Parcel> calculateNearestNeighborRoute(List<Parcel> parcels) {
        List<Parcel> unvisited = new ArrayList<>(parcels);
        List<Parcel> route = new ArrayList<>();
        
        double currentLat = DEPOT_LAT;
        double currentLng = DEPOT_LNG;

        while (!unvisited.isEmpty()) {
            Parcel nearest = null;
            double shortestDistance = Double.MAX_VALUE;

            for (Parcel candidate : unvisited) {
                double distance = haversine(currentLat, currentLng, candidate.getLatitude(), candidate.getLongitude());
                if (distance < shortestDistance) {
                    shortestDistance = distance;
                    nearest = candidate;
                }
            }

            if (nearest != null) {
                route.add(nearest);
                unvisited.remove(nearest);
                currentLat = nearest.getLatitude();
                currentLng = nearest.getLongitude();
            }
        }
        return route;
    }

    // Mathematical formula to calculate exact distance between two coordinates on a sphere
    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Earth radius in kilometers

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}