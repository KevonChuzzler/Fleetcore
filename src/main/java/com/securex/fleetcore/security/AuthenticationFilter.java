package com.securex.fleetcore.security;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    private static final String REALM = "Bearer";

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // Skip authentication for login/public endpoints if necessary
        String path = requestContext.getUriInfo().getPath();
        if (path.contains("public") || path.contains("login")) {
            return;
        }

        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader == null || !authorizationHeader.startsWith(REALM + " ")) {
            requestContext.abortWith(
                Response.status(Response.Status.UNAUTHORIZED)
                        .entity("Authorization token is required.")
                        .build()
            );
            return;
        }

        String token = authorizationHeader.substring(REALM.length()).trim();

        try {
            validateToken(token);
            // Future implementation: Extract user role from token and set SecurityContext for RBAC
        } catch (Exception e) {
            requestContext.abortWith(
                Response.status(Response.Status.UNAUTHORIZED)
                        .entity("Invalid or expired token.")
                        .build()
            );
        }
    }

    private void validateToken(String token) throws Exception {
        // Placeholder for Google OAuth token validation logic
        // E.g., using GoogleIdTokenVerifier
        if (token.isEmpty()) {
            throw new Exception("Token is empty");
        }
    }
}