package com.securex.fleetcore.controller;

import com.securex.fleetcore.entity.MaintenanceRecord;
import com.securex.fleetcore.entity.MaintenanceSchedule;
import com.securex.fleetcore.entity.Vehicle;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.LocalDate;

@Path("/maintenance")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MaintenanceController {

    @PersistenceContext
    private EntityManager entityManager;

    public static class RecordPayload {
        public Long vehicleId;
        public String serviceType;
        public String description;
        public Double cost;
        public Double odometerKm;
    }

    public static class SchedulePayload {
        public Long vehicleId;
        public String taskName;
        public String targetDate; // Format: YYYY-MM-DD
        public Double targetOdometerKm;
    }

    @POST
    @Path("/records")
    @Transactional
    public Response addMaintenanceRecord(RecordPayload payload) {
        Vehicle vehicle = entityManager.find(Vehicle.class, payload.vehicleId);
        if (vehicle == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Vehicle not found.").build();
        }

        MaintenanceRecord record = new MaintenanceRecord();
        record.setVehicle(vehicle);
        record.setServiceType(payload.serviceType);
        record.setDescription(payload.description);
        record.setCost(payload.cost);
        record.setOdometerKm(payload.odometerKm);

        entityManager.persist(record);
        return Response.status(Response.Status.CREATED).entity(record).build();
    }

    @POST
    @Path("/schedules")
    @Transactional
    public Response createMaintenanceSchedule(SchedulePayload payload) {
        Vehicle vehicle = entityManager.find(Vehicle.class, payload.vehicleId);
        if (vehicle == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Vehicle not found.").build();
        }

        MaintenanceSchedule schedule = new MaintenanceSchedule();
        schedule.setVehicle(vehicle);
        schedule.setTaskName(payload.taskName);
        schedule.setTargetDate(LocalDate.parse(payload.targetDate));
        schedule.setTargetOdometerKm(payload.targetOdometerKm);
        schedule.setStatus("PENDING");

        entityManager.persist(schedule);
        return Response.status(Response.Status.CREATED).entity(schedule).build();
    }
}