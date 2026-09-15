package com.securex.fleetcore.service;

import jakarta.enterprise.context.ApplicationScoped;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.logging.Logger;

@ApplicationScoped
public class RouteOptimizationService {

    private static final Logger LOGGER = Logger.getLogger(RouteOptimizationService.class.getName());
    private static final String GMPRO_URL = "https://routes.googleapis.com/directions/v2:computeRoutes";
    private static final String GMPRO_API_KEY = System.getenv("GOOGLE_MAPS_API_KEY");

    private final HttpClient httpClient = HttpClient.newHttpClient();

    public String optimizeRoute(String originJson, String destinationJson, String waypointsJson) {
        // Construct the payload required by Google Maps Routes API
        String requestBody = String.format(
            "{ \"origin\": %s, \"destination\": %s, \"intermediates\": %s, \"travelMode\": \"DRIVE\" }", 
            originJson, destinationJson, waypointsJson
        );

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(GMPRO_URL))
                    .header("Content-Type", "application/json")
                    .header("X-Goog-Api-Key", GMPRO_API_KEY)
                    .header("X-Goog-FieldMask", "routes.duration,routes.distanceMeters,routes.polyline.encodedPolyline")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 200) {
                return response.body();
            } else {
                LOGGER.warning("Route optimization failed. HTTP Status: " + response.statusCode() + " Body: " + response.body());
            }
        } catch (Exception e) {
            LOGGER.severe("Error calling Google Maps API: " + e.getMessage());
        }
        return null;
    }
}