package com.securex.fleetcore.service;

import com.securex.fleetcore.websocket.TelemetryWebSocket;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Schedule;
import jakarta.ejb.Singleton;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

import java.io.InputStream;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class GoogleSimulatorService {

    private String googleApiKey;

    @Inject
    private TelemetryWebSocket telemetryWebSocket;

    private final Map<Long, List<double[]>> activeRoutes = new ConcurrentHashMap<>();
    private final HttpClient httpClient = HttpClient.newHttpClient();

    @PostConstruct
    public void init() {
        // First check environment variable
        this.googleApiKey = System.getenv("GOOGLE_API_KEY");

        // Fallback to microprofile-config.properties if present
        if (this.googleApiKey == null || this.googleApiKey.isBlank()) {
            try (InputStream input = getClass().getClassLoader().getResourceAsStream("META-INF/microprofile-config.properties")) {
                if (input != null) {
                    Properties prop = new Properties();
                    prop.load(input);
                    this.googleApiKey = prop.getProperty("GOOGLE_API_KEY");
                }
            } catch (Exception e) {
                System.err.println("[GoogleSimulator] Could not load microprofile-config.properties: " + e.getMessage());
            }
        }
    }

    public void startSimulation(Long vehicleId, String registration, double startLat, double startLng, double endLat, double endLng) {
        if (googleApiKey == null || googleApiKey.isBlank()) {
            System.err.println("[GoogleSimulator] ERROR: GOOGLE_API_KEY is not set!");
            return;
        }

        try {
            String url = String.format(
                "https://maps.googleapis.com/maps/api/directions/json?origin=%f,%f&destination=%f,%f&key=%s",
                startLat, startLng, endLat, endLng, googleApiKey
            );

            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            try (JsonReader reader = Json.createReader(new StringReader(response.body()))) {
                JsonObject jsonObject = reader.readObject();
                JsonArray routes = jsonObject.getJsonArray("routes");

                if (routes != null && !routes.isEmpty()) {
                    String encodedPolyline = routes.getJsonObject(0)
                        .getJsonObject("overview_polyline")
                        .getString("points");

                    List<double[]> routePoints = decodePolyline(encodedPolyline);
                    activeRoutes.put(vehicleId, routePoints);
                    System.out.println("[GoogleSimulator] Route loaded for " + registration + " (" + routePoints.size() + " waypoints).");
                } else {
                    System.err.println("[GoogleSimulator] Google API returned no routes for coordinates.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<double[]> decodePolyline(String encoded) {
        List<double[]> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0; result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            poly.add(new double[]{ (lat / 1E5), (lng / 1E5) });
        }
        return poly;
    }

    @Schedule(hour = "*", minute = "*", second = "*/3", persistent = false)
    public void moveVehicles() {
        if (activeRoutes.isEmpty()) return;

        activeRoutes.forEach((vehicleId, route) -> {
            if (!route.isEmpty()) {
                double[] point = route.remove(0);
                double lat = point[0];
                double lng = point[1];

                String telemetryJson = String.format(
                    "{\"vehicleId\": %d, \"lat\": %.6f, \"lng\": %.6f, \"speed\": 71.2}",
                    vehicleId, lat, lng
                );

                if (telemetryWebSocket != null) {
                    telemetryWebSocket.broadcast(telemetryJson);
                }

                if (route.isEmpty()) {
                    activeRoutes.remove(vehicleId);
                    System.out.println("[GoogleSimulator] Vehicle " + vehicleId + " arrived at destination.");
                }
            }
        });
    }
}