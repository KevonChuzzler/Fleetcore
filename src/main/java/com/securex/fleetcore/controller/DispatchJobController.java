package com.securex.fleetcore.controller;

import com.securex.fleetcore.entity.DispatchJob;
import com.securex.fleetcore.entity.Parcel;
import com.securex.fleetcore.entity.Vehicle;
import com.securex.fleetcore.service.GoogleSimulatorService;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.LocalDateTime;

@Path("/dispatch/jobs")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DispatchJobController {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private GoogleSimulatorService simulatorService;

    @POST
    @Path("/vehicle/{vehicleId}")
    @Transactional
    public Response createJob(@PathParam("vehicleId") Long vehicleId) {
        Vehicle vehicle = em.find(Vehicle.class, vehicleId);
        if (vehicle == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Vehicle not found").build();
        }

        DispatchJob job = new DispatchJob();
        job.setVehicle(vehicle);
        job.setStatus("CREATED");
        job.setCreatedAt(LocalDateTime.now());
        em.persist(job);

        return Response.status(Response.Status.CREATED).entity(job).build();
    }

    @PUT
    @Path("/{jobId}/parcel/{parcelId}")
    @Transactional
    public Response assignParcel(@PathParam("jobId") Long jobId, @PathParam("parcelId") Long parcelId) {
        DispatchJob job = em.find(DispatchJob.class, jobId);
        Parcel parcel = em.find(Parcel.class, parcelId);

        if (job == null || parcel == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Job or Parcel not found").build();
        }

        job.setParcel(parcel);
        em.merge(job);

        return Response.ok(job).build();
    }

    @PUT
    @Path("/{jobId}/status/{status}")
    @Transactional
    public Response updateJobStatus(@PathParam("jobId") Long jobId, @PathParam("status") String status) {
        DispatchJob job = em.find(DispatchJob.class, jobId);
        if (job == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Job not found").build();
        }

        job.setStatus(status);
        if ("IN_PROGRESS".equalsIgnoreCase(status)) {
            job.setStartTime(LocalDateTime.now());
        } else if ("COMPLETED".equalsIgnoreCase(status)) {
            job.setEndTime(LocalDateTime.now());
        }
        em.merge(job);

        // Trigger simulation when the job starts with the new exact depot coordinates
        if ("IN_PROGRESS".equalsIgnoreCase(status) && job.getVehicle() != null && job.getParcel() != null) {
            double depotLat = -25.462288460579988;
            double depotLng = 30.984694233996766;

            double destLat = job.getParcel().getLatitude();
            double destLng = job.getParcel().getLongitude();

            simulatorService.startSimulation(
                job.getVehicle().getId(),
                job.getVehicle().getRegistrationNumber(),
                depotLat, depotLng,
                destLat, destLng
            );
        }

        return Response.ok(job).build();
    }
}