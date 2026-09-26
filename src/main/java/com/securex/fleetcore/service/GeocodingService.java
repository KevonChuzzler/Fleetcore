package com.securex.fleetcore.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

import java.io.StringReader;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

@ApplicationScoped
public class GeocodingService {

    private final String apiKey = "db493c04fb8f4578bfa280a617f843e0";
    private static final String GEOCODE_URL = "https://api.opencagedata.com/geocode/v1/json?q=%s&key=%s";
    
    private final HttpClient httpClient = HttpClient.newHttpClient();

    // Now returns a simple array: [latitude, longitude]
    public Double[] geocodeAddress(String address) {
        if (apiKey == null || apiKey.isEmpty()) {
            System.err.println("API Key is missing!");
            return null;
        }

        try {
            String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8);
            String requestUrl = String.format(GEOCODE_URL, encodedAddress, apiKey);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(requestUrl))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return parseCoordinates(response.body());
            } else {
                System.err.println("OpenCage geocoding failed with status: " + response.statusCode());
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Double[] parseCoordinates(String jsonResponse) {
        try (JsonReader reader = Json.createReader(new StringReader(jsonResponse))) {
            JsonObject jsonObject = reader.readObject();
            
            JsonObject geometry = jsonObject.getJsonArray("results")
                                            .getJsonObject(0)
                                            .getJsonObject("geometry");
            
            Double lat = geometry.getJsonNumber("lat").doubleValue();
            Double lng = geometry.getJsonNumber("lng").doubleValue();

            return new Double[]{lat, lng};
        } catch (Exception e) {
            System.err.println("Could not parse OpenCage JSON: " + jsonResponse);
            return null;
        }
    }
}