package com.securex.fleetcore.config;

import com.securex.fleetcore.entity.Driver;
import com.securex.fleetcore.entity.FuelLog;
import com.securex.fleetcore.entity.MaintenanceRecord;
import com.securex.fleetcore.entity.Parcel;
import com.securex.fleetcore.entity.Vehicle;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Singleton
@Startup
public class MockDataSeeder {

    @PersistenceContext
    private EntityManager entityManager;

    @PostConstruct
    @Transactional
    public void seedDatabase() {
        Long vehicleCount = entityManager.createQuery("SELECT COUNT(v) FROM Vehicle v", Long.class).getSingleResult();
        
        if (vehicleCount > 0) {
            System.out.println("====== MockDataSeeder: Database already populated. Skipping seeding. ======");
            return;
        }

        System.out.println("====== MockDataSeeder: Injecting heavy fleet and drivers... ======");

        // 1. Create 18 Drivers
        String[] firstNames = {"Sipho", "Thabo", "Johan", "Pieter", "Sibusiso", "Bongani", "Lungile", "Willem", "David", "Jaco", "Kagiso", "Lebohang", "Mandla", "Nathi", "Oupa", "Quinton", "Ruan", "Tebogo"};
        String[] lastNames = {"Nkosi", "Dlamini", "Botha", "Van Der Merwe", "Ndlovu", "Khumalo", "Mokoena", "Smit", "Jones", "Venter", "Mahlangu", "Modise", "Zwane", "Phiri", "Baloyi", "Naidoo", "Pretorius", "Mthembu"};
        
        List<Driver> drivers = new ArrayList<>();
        for (int i = 0; i < 18; i++) {
            Driver d = new Driver();
            d.setFirstName(firstNames[i]);
            d.setLastName(lastNames[i]);
            d.setLicenseNumber("ZA" + (900000 + i));
            d.setLicenseExpiryDate(LocalDate.now().plusYears(1).plusMonths(i));
            d.setContactNumber("08255500" + String.format("%02d", i));
            d.setStatus(i % 5 == 0 ? "ON_LEAVE" : "ACTIVE"); // Mix of Active and On Leave
            entityManager.persist(d);
            drivers.add(d);
        }

        List<Vehicle> fleet = new ArrayList<>();
        String[] statuses = {"AVAILABLE", "IN_TRANSIT", "MAINTENANCE", "AVAILABLE"};

        // 2. Create 8x Mercedes Axor Trucks
        for (int i = 0; i < 8; i++) {
            fleet.add(createVehicle("Mercedes-Benz", "Axor", "TRUCK", "AXR-" + (100 + i) + "-MP", statuses[i % 4]));
        }

        // 3. Create 4x Scania R560 Trucks
        for (int i = 0; i < 4; i++) {
            fleet.add(createVehicle("Scania", "R560", "TRUCK", "R560-" + (100 + i) + "-MP", statuses[(i + 1) % 4]));
        }

        // 4. Create 4x Scania G500 Trucks
        for (int i = 0; i < 4; i++) {
            fleet.add(createVehicle("Scania", "G500", "TRUCK", "G500-" + (100 + i) + "-MP", statuses[(i + 2) % 4]));
        }

        // 5. Create 4x Toyota Hilux 2.4 GD-6 (Small Vehicles)
        for (int i = 0; i < 4; i++) {
            fleet.add(createVehicle("Toyota", "Hilux 2.4 GD-6", "LIGHT_VEHICLE", "HLX-" + (100 + i) + "-MP", statuses[i % 4]));
        }

        // 6. Create 2x Toyota Auris (Small Vehicles)
        for (int i = 0; i < 2; i++) {
            fleet.add(createVehicle("Toyota", "Auris", "LIGHT_VEHICLE", "AUR-" + (100 + i) + "-MP", "AVAILABLE"));
        }

        // 7. Assign the 18 drivers to the first 18 vehicles (leaving 4 vehicles unassigned)
        for (int i = 0; i < 18; i++) {
            Vehicle v = fleet.get(i);
            v.setDriver(drivers.get(i));
            entityManager.persist(v);
        }
        
        // Persist the remaining unassigned vehicles
        for (int i = 18; i < fleet.size(); i++) {
            entityManager.persist(fleet.get(i));
        }

        // 8. Create some Mock Parcels for testing the routing engine
        String[] clients = {"Acme Corp", "Global Tech", "Builders Warehouse", "Takealot Hub"};
        for (int i = 0; i < 6; i++) {
            Parcel p = new Parcel();
            p.setClientName(clients[i % clients.length]);
            p.setDeliveryAddress("Delivery Address " + i + ", Nelspruit");
            p.setWeightKg(25.0 + (i * 10)); // Heavy packages for trucks
            entityManager.persist(p);
        }

        // 9. Add some Analytics Data to Vehicle 1 (Mercedes Axor) for testing
        FuelLog fuelLog = new FuelLog();
        fuelLog.setVehicle(fleet.get(0));
        fuelLog.setLiters(250.0);
        fuelLog.setTotalCost(5800.00);
        fuelLog.setOdometerKm(125000.0);
        fuelLog.setDate(LocalDate.now());
        entityManager.persist(fuelLog);

        MaintenanceRecord maintenance = new MaintenanceRecord();
        maintenance.setVehicle(fleet.get(0));
        maintenance.setServiceType("ROUTINE_SERVICE");
        maintenance.setDescription("100,000km Major Service");
        maintenance.setCost(14500.00);
        maintenance.setOdometerKm(120000.0);
        maintenance.setServiceDate(LocalDate.now().minusDays(15));
        entityManager.persist(maintenance);

        System.out.println("====== MockDataSeeder: Fleet successfully injected! (" + fleet.size() + " Vehicles, " + drivers.size() + " Drivers) ======");
    }

    private Vehicle createVehicle(String make, String model, String type, String reg, String status) {
        Vehicle v = new Vehicle();
        v.setMake(make);
        v.setModel(model);
        v.setVehicleType(type);
        v.setRegistrationNumber(reg);
        v.setStatus(status);
        v.setMileage(Math.random() * 200000); 
        return v;
    }
}