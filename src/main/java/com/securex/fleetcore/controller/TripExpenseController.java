package com.securex.fleetcore.controller;

import com.securex.fleetcore.entity.TripExpense;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@RequestScoped
@Path("/expenses")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TripExpenseController {

    @PersistenceContext
    private EntityManager entityManager;

    @GET
    public List<TripExpense> getAllExpenses() {
        return entityManager.createQuery("SELECT e FROM TripExpense e", TripExpense.class).getResultList();
    }

    @POST
    @Transactional
    public Response logExpense(TripExpense expense) {
        entityManager.persist(expense);
        return Response.status(Response.Status.CREATED).entity(expense).build();
    }
}