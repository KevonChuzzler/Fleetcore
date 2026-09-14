package com.securex.fleetcore.controller;

import com.securex.fleetcore.dao.MaintenanceRecordDAO;
import com.securex.fleetcore.entity.MaintenanceRecord;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/maintenance")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MaintenanceController {

    @Inject
    private MaintenanceRecordDAO maintenanceDAO;

    @GET
    public List<MaintenanceRecord> getAllMaintenanceRecords() {
        return maintenanceDAO.findAll(); // Retrieves maintenance schedules and service records[cite: 1]
    }

    @POST
    public Response createMaintenanceRecord(MaintenanceRecord record) {
        maintenanceDAO.create(record); // Schedules and records vehicle maintenance[cite: 1]
        return Response.status(Response.Status.CREATED).entity(record).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateMaintenanceRecord(@PathParam("id") Long id, MaintenanceRecord updatedRecord) {
        MaintenanceRecord existingRecord = maintenanceDAO.findById(id);
        if (existingRecord == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        updatedRecord.setMaintenanceId(id);
        maintenanceDAO.update(updatedRecord);
        return Response.ok(updatedRecord).build();
    }
}