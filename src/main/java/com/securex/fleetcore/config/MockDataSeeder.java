package com.securex.fleetcore.config;

import com.securex.fleetcore.entity.Driver;
import com.securex.fleetcore.entity.FuelLog;
import com.securex.fleetcore.entity.MaintenanceRecord;
import com.securex.fleetcore.entity.Parcel;
import com.securex.fleetcore.entity.TripExpense;
import com.securex.fleetcore.entity.Vehicle;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Singleton
@Startup
public class MockDataSeeder {

    @PersistenceContext
    private EntityManager em;

    @PostConstruct
    @Transactional
    public void seedDatabase() {
        Long vehicleCount = em.createQuery("SELECT COUNT(v) FROM Vehicle v", Long.class).getSingleResult();
        if (vehicleCount > 0) {
            return; // Database already seeded
        }

        Random rand = new Random();
        List<Vehicle> persistedVehicles = new ArrayList<>();

        // 1. Seed 18 Drivers
        String[] firstNames = {"Sipho", "Johan", "Thabo", "Pieter", "Musa", "Bongani", "Willem", "Jaco", "Sibusiso", "Kabelo", "Lunga", "Riaan", "Dirk", "Mandla", "Tshepo", "Anton", "Heinrich", "Vusi"};
        String[] lastNames = {"Nkosi", "van der Merwe", "Mokoena", "de Klerk", "Dlamini", "Zwane", "Botha", "Fourie", "Ndlovu", "Molefe", "Khumalo", "Pretorius", "Coetzee", "Mahlangu", "Morake", "Smit", "Meyer", "Vilakazi"};
        
        for (int i = 0; i < 18; i++) {
            Driver d = new Driver();
            d.setFirstName(firstNames[i]);
            d.setLastName(lastNames[i]);
            d.setLicenseNumber("ZA-" + (100000 + rand.nextInt(899999)));
            d.setContactNumber("0" + (70 + rand.nextInt(14)) + rand.nextInt(10000000));
            d.setLicenseExpiry(LocalDate.now().plusMonths(rand.nextInt(36) + 6));
            d.setStatus(i % 5 == 0 ? "ON_LEAVE" : "ACTIVE");
            em.persist(d);
        }

        // 2. Seed 22 Vehicles & Placement Coordinates
        String[] models = {
            "Axor", "Axor", "Axor", "Axor", "Axor", "Axor", "Axor", "Axor",
            "R500", "R500", "R500", "R500", "R500", "R500", "R500", "R500",
            "Hilux", "Hilux", "Hilux", "Auris", "Auris", "Auris"
        };
        String[] makes = {
            "Mercedes", "Mercedes", "Mercedes", "Mercedes", "Mercedes", "Mercedes", "Mercedes", "Mercedes",
            "Scania", "Scania", "Scania", "Scania", "Scania", "Scania", "Scania", "Scania",
            "Toyota", "Toyota", "Toyota", "Toyota", "Toyota", "Toyota"
        };

        // Exact Depot coordinates
        double depotLat = -25.462288460579988;
        double depotLng = 30.984694233996766;

        // Hardcoded real-world road coordinates in Mbombela (R40, N4, etc.)
        double[][] roadCoords = {
            {-25.4580, 30.9805}, {-25.4675, 30.9950}, {-25.4740, 30.9780},
            {-25.4500, 30.9820}, {-25.4850, 30.9700}, {-25.4610, 30.9880},
            {-25.4712, 30.9910}, {-25.4555, 30.9755}, {-25.4800, 30.9650},
            {-25.4480, 30.9850}, {-25.4650, 31.0000}, {-25.4520, 30.9790},
            {-25.4600, 30.9920}, {-25.4750, 30.9850}, {-25.4680, 30.9720}
        };

        int aurisAtDepot = 0;
        int hiluxAtDepot = 0;

        for (int i = 0; i < 22; i++) {
            Vehicle v = new Vehicle();
            v.setMake(makes[i]);
            v.setModel(models[i]);
            v.setRegistrationNumber(String.format("FCT-%03d-MP", i + 1));
            v.setVehicleType(makes[i].equals("Toyota") ? "LIGHT_VEHICLE" : "TRUCK");

            // Hold exactly 1 Auris and 2 Hiluxes at the depot
            boolean keepAtDepot = false;
            if (models[i].equals("Auris") && aurisAtDepot < 1) {
                keepAtDepot = true;
                aurisAtDepot++;
            } else if (models[i].equals("Hilux") && hiluxAtDepot < 2) {
                keepAtDepot = true;
                hiluxAtDepot++;
            }

            if (keepAtDepot) {
                v.setLatitude(depotLat);
                v.setLongitude(depotLng);
                v.setStatus("AVAILABLE");
            } else {
                v.setLatitude(roadCoords[i % roadCoords.length][0]);
                v.setLongitude(roadCoords[i % roadCoords.length][1]);
                v.setStatus(i % 6 == 0 ? "MAINTENANCE" : "IN_TRANSIT");
            }
            em.persist(v);
            persistedVehicles.add(v);
        }

        // 3. Seed Analytics & Financial Data
        for (Vehicle v : persistedVehicles) {
            for (int i = 0; i < 3; i++) {
                FuelLog fuel = new FuelLog();
                fuel.setVehicle(v);
                double liters = 50.0 + (rand.nextDouble() * 300.0);
                fuel.setLiters(liters);
                fuel.setCost(liters * 23.50);
                fuel.setTotalCost(liters * 23.50);
                fuel.setOdometerKm(15000.0 + rand.nextInt(5000));
                fuel.setTimestamp(LocalDateTime.now().minusDays(rand.nextInt(30)));
                em.persist(fuel);
            }

            for (int i = 0; i < 2; i++) {
                TripExpense exp = new TripExpense();
                exp.setVehicle(v);
                exp.setDescription(i % 2 == 0 ? "N4 Nkomazi Toll Plaza" : "Overnight Driver Allowance");
                exp.setCategory(i % 2 == 0 ? "TOLL" : "ALLOWANCE");
                exp.setAmount(120.0 + rand.nextInt(400));
                exp.setTimestamp(LocalDateTime.now().minusDays(rand.nextInt(30)));
                em.persist(exp);
            }

            MaintenanceRecord maint = new MaintenanceRecord();
            maint.setVehicle(v);
            maint.setDescription("Scheduled 10,000km Major Service & Brake Pad Replacement");
            maint.setServiceType("ROUTINE");
            maint.setOdometerKm(20000.0 + rand.nextInt(5000));
            maint.setCost(4500.0 + rand.nextInt(15000));
            maint.setDate(LocalDate.now().minusDays(rand.nextInt(60)));
            em.persist(maint);
        }

        // 4. Seed 15 PENDING Parcels 
        String[] clients = {"Makro Nelspruit", "SPAR White River", "Kruger National Park", "BuildIt Malelane", "Barberton Mines", "Hazyview Mall", "Boulders Lodge"};
        String[] addresses = {
            "12 Brown St, Nelspruit", "Chief Albert Luthuli St, White River", 
            "Paul Kruger Gate, Skukuza", "Air St, Malelane", 
            "Sheba Rd, Barberton", "R40 Main Rd, Hazyview"
        };
        
        for (int i = 0; i < 15; i++) {
            Parcel p = new Parcel();
            p.setClientName(clients[rand.nextInt(clients.length)]);
            p.setDeliveryAddress(addresses[rand.nextInt(addresses.length)]);
            p.setWeightKg(15.5 + rand.nextDouble() * 500.0);
            p.setStatus("PENDING");
            // Scatter around the new depot coordinates
            p.setLatitude(depotLat + (rand.nextDouble() * 0.4 - 0.2));
            p.setLongitude(depotLng + (rand.nextDouble() * 0.4 - 0.2));
            em.persist(p);
        }
    }
}