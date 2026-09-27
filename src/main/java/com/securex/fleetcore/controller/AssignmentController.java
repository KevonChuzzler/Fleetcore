package com.securex.fleetcore.controller;

import com.securex.fleetcore.entity.DispatchJob;
import com.securex.fleetcore.entity.Parcel;
import com.securex.fleetcore.entity.Vehicle;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.List;

@Path("/dispatch")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AssignmentController {

    @PersistenceContext
    private EntityManager entityManager;

    @GET
    @Path("/jobs")
    public List<DispatchJob> getAllJobs() {
        return entityManager.createQuery("SELECT d FROM DispatchJob d", DispatchJob.class).getResultList();
    }

    // 1. Create a new dispatch schedule for a specific truck
    @POST
    @Path("/jobs/vehicle/{vehicleId}")
    @Transactional
    public Response createJob(@PathParam("vehicleId") Long vehicleId) {
        Vehicle vehicle = entityManager.find(Vehicle.class, vehicleId);
        if (vehicle == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Vehicle not found").build();
        }

        DispatchJob job = new DispatchJob();
        job.setVehicle(vehicle);
        job.setStatus("SCHEDULED");
        entityManager.persist(job);
        
        return Response.status(Response.Status.CREATED).entity(job).build();
    }

    // 2. Assign a specific parcel to the dispatch manifest
    @PUT
    @Path("/jobs/{jobId}/parcel/{parcelId}")
    @Transactional
    public Response assignParcelToJob(@PathParam("jobId") Long jobId, @PathParam("parcelId") Long parcelId) {
        DispatchJob job = entityManager.find(DispatchJob.class, jobId);
        Parcel parcel = entityManager.find(Parcel.class, parcelId);
        
        if (job == null || parcel == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Job or Parcel not found").build();
        }

        parcel.setDispatchJob(job);
        parcel.setStatus("ASSIGNED");
        entityManager.merge(parcel);
        
        // Refresh job to reflect new parcel in the returned JSON
        entityManager.refresh(job);
        return Response.ok(job).build();
    }

    // 3. Update Workflow Lifecycle (Cascades to Vehicle and Parcels)
    @PUT
    @Path("/jobs/{jobId}/status/{status}")
    @Transactional
    public Response updateJobStatus(@PathParam("jobId") Long jobId, @PathParam("status") String status) {
        DispatchJob job = entityManager.find(DispatchJob.class, jobId);
        if (job == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Job not found").build();
        }

        String formattedStatus = status.toUpperCase();
        job.setStatus(formattedStatus);

        if (formattedStatus.equals("IN_PROGRESS")) {
            job.setStartTime(LocalDateTime.now());
            job.getVehicle().setStatus("IN_TRANSIT");
            for (Parcel p : job.getParcels()) {
                p.setStatus("IN_TRANSIT");
                entityManager.merge(p);
            }
        } else if (formattedStatus.equals("COMPLETED")) {
            job.setEndTime(LocalDateTime.now());
            job.getVehicle().setStatus("AVAILABLE");
            for (Parcel p : job.getParcels()) {
                p.setStatus("DELIVERED");
                entityManager.merge(p);
            }
        }

        entityManager.merge(job);
        entityManager.merge(job.getVehicle());

        return Response.ok(job).build();
    }
}