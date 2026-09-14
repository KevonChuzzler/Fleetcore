package com.securex.fleetcore.controller;

import com.securex.fleetcore.dao.FuelRecordDAO;
import com.securex.fleetcore.entity.FuelRecord;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/fuel")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FuelController {

    @Inject
    private FuelRecordDAO fuelDAO;

    @GET
    public List<FuelRecord> getAllFuelRecords() {
        return fuelDAO.findAll(); // Retrieves fuel usage and consumption information[cite: 1]
    }

    @POST
    public Response addFuelRecord(FuelRecord record) {
        fuelDAO.create(record); // Records fuel usage, fuel costs and vehicle mileage[cite: 1]
        return Response.status(Response.Status.CREATED).entity(record).build();
    }
}