package com.securex.fleetcore.controller;

import com.securex.fleetcore.entity.Parcel;
import com.securex.fleetcore.service.GeocodingService;
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

@Path("/parcels")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ParcelController {

    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private GeocodingService geocodingService;

    @POST
    @Transactional
    public Response createParcel(Parcel parcel) {
        // Geocode the address
        Double[] coords = geocodingService.geocodeAddress(parcel.getDeliveryAddress());
        
        if (coords != null) {
            parcel.setLatitude(coords[0]);
            parcel.setLongitude(coords[1]);
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Could not geocode the delivery address.")
                    .build();
        }

        entityManager.persist(parcel);
        return Response.status(Response.Status.CREATED).entity(parcel).build();
    }
}