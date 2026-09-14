package com.securex.fleetcore.service;

import jakarta.enterprise.context.ApplicationScoped;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.logging.Logger;

@ApplicationScoped
public class TrackerApiService {

    private static final Logger LOGGER = Logger.getLogger(TrackerApiService.class.getName());
    private static final String TRACKER_API_URL = "https://api.external-tracker.com/v1/vehicles/"; // Replace with actual API provider
    private static final String API_KEY = System.getenv("TRACKER_API_KEY");

    private final HttpClient httpClient = HttpClient.newHttpClient();

    public String fetchLatestLocation(String vehicleRegistration) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(TRACKER_API_URL + vehicleRegistration + "/location"))
                    .header("Authorization", "Bearer " + API_KEY)
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 200) {
                return response.body(); // Returns JSON containing latitude, longitude, and timestamp
            } else {
                LOGGER.warning("Failed to fetch GPS data. HTTP Status: " + response.statusCode());
            }
        } catch (Exception e) {
            LOGGER.severe("Error calling Tracker API: " + e.getMessage());
        }
        return null;
    }
}