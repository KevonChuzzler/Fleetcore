package com.securex.fleetcore.security;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.util.Collections;

//@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    // You will replace this with your actual Google Cloud Project Client ID
    private static final String CLIENT_ID = "482782765344-qiptkt00s999f7sc281kbu1pt2u0auo9.apps.googleusercontent.com.apps.googleusercontent.com";

    // Sets up the cryptographic verifier calling out to Google's public keys
    private final GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
            new NetHttpTransport(),
            new GsonFactory())
            .setAudience(Collections.singletonList(CLIENT_ID))
            .build();

    @Override
    public void filter(ContainerRequestContext requestContext) {
        // 1. Get the Authorization header from the incoming request
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            abortWithUnauthorized(requestContext, "Missing or invalid Authorization header");
            return;
        }

        // 2. Extract the JWT string
        String token = authorizationHeader.substring("Bearer".length()).trim();

        try {
            // 3. Cryptographically verify the token with Google
            GoogleIdToken idToken = verifier.verify(token);
            
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();
                
                // Success! You can now extract user info if needed:
                // String email = payload.getEmail();
                // requestContext.setProperty("userEmail", email);
                
            } else {
                abortWithUnauthorized(requestContext, "Invalid Google ID token.");
            }
        } catch (Exception e) {
            abortWithUnauthorized(requestContext, "Token verification failed: " + e.getMessage());
        }
    }

    private void abortWithUnauthorized(ContainerRequestContext requestContext, String message) {
        requestContext.abortWith(
                Response.status(Response.Status.UNAUTHORIZED)
                        .entity("{\"error\":\"" + message + "\"}")
                        .build());
    }
}