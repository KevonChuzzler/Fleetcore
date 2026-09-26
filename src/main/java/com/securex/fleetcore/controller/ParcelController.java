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