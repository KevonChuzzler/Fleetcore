package com.securex.fleetcore.controller;

import com.securex.fleetcore.dao.NotificationDAO;
import com.securex.fleetcore.entity.Notification;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/notifications")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NotificationController {

    @Inject
    private NotificationDAO notificationDAO;

    @GET
    public List<Notification> getAllNotifications() {
        return notificationDAO.findAll(); // Retrieves alerts and reminders[cite: 1]
    }

    @PUT
    @Path("/{id}/read")
    public Response markAsRead(@PathParam("id") Long id) {
        Notification notification = notificationDAO.findById(id);
        if (notification == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        notification.setIsRead(true);
        notificationDAO.update(notification);
        return Response.ok(notification).build();
    }
}