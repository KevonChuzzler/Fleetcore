package com.securex.fleetcore.controller;

import com.securex.fleetcore.dao.UserDAO;
import com.securex.fleetcore.entity.User;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {

    @Inject
    private UserDAO userDAO;

    @GET
    public List<User> getAllUsers() {
        return userDAO.findAll(); // Retrieves user accounts for access control management[cite: 1]
    }

    @POST
    public Response createUser(User user) {
        userDAO.create(user); // Manages user accounts and permissions according to user roles[cite: 1]
        return Response.status(Response.Status.CREATED).entity(user).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteUser(@PathParam("id") Long id) {
        User existingUser = userDAO.findById(id);
        if (existingUser == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        userDAO.delete(id);
        return Response.noContent().build();
    }
}