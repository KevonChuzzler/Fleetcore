package com.securex.fleetcore.controller;

import com.securex.fleetcore.entity.TripExpense;
import com.securex.fleetcore.entity.Vehicle;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/expenses")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TripExpenseController {

    @PersistenceContext
    private EntityManager entityManager;

    public static class ExpensePayload {
        public Long vehicleId;
        public String category;
        public Double amount;
        public String description;
    }

    @POST
    @Transactional
    public Response addExpense(ExpensePayload payload) {
        Vehicle vehicle = entityManager.find(Vehicle.class, payload.vehicleId);
        if (vehicle == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Vehicle not found.").build();
        }

        TripExpense expense = new TripExpense();
        expense.setVehicle(vehicle);
        expense.setCategory(payload.category);
        expense.setAmount(payload.amount);
        expense.setDescription(payload.description);

        entityManager.persist(expense);
        return Response.status(Response.Status.CREATED).entity(expense).build();
    }
}