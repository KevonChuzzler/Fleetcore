package com.securex.fleetcore.security;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.container.PreMatching;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
@PreMatching   // <-- CRITICAL FOR WILDFLY: runs BEFORE JAX-RS resource matching
@Priority(Priorities.AUTHENTICATION - 100) // <-- CRITICAL: runs BEFORE any Auth filter!
public class CorsFilter implements ContainerRequestFilter, ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // Abort early with 200 OK on preflight OPTIONS requests so browser can proceed
        if (requestContext.getMethod().equalsIgnoreCase("OPTIONS")) {
            String origin = requestContext.getHeaderString("Origin");
            if (origin == null || origin.isEmpty()) {
                origin = "*";
            }
            requestContext.abortWith(Response.ok()
                .header("Access-Control-Allow-Origin", origin)
                .header("Access-Control-Allow-Credentials", "true")
                .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization, x-requested-with")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
                .header("Access-Control-Max-Age", "3600")
                .build());
        }
    }

    @Override
    public void filter(ContainerRequestContext requestContext, 
                       ContainerResponseContext responseContext) throws IOException {
        String origin = requestContext.getHeaderString("Origin");
        if (origin != null && !origin.isEmpty()) {
            responseContext.getHeaders().putSingle("Access-Control-Allow-Origin", origin);
        } else {
            responseContext.getHeaders().putSingle("Access-Control-Allow-Origin", "*");
        }
        responseContext.getHeaders().putSingle("Access-Control-Allow-Credentials", "true");
        responseContext.getHeaders().putSingle("Access-Control-Allow-Headers", "origin, content-type, accept, authorization, x-requested-with");
        responseContext.getHeaders().putSingle("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
    }
}