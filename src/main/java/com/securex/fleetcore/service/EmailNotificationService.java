package com.securex.fleetcore.service;

import jakarta.enterprise.context.ApplicationScoped;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.logging.Logger;

@ApplicationScoped
public class EmailNotificationService {

    private static final Logger LOGGER = Logger.getLogger(EmailNotificationService.class.getName());
    private static final String RESEND_API_URL = "https://api.resend.com/emails";
    private static final String RESEND_API_KEY = System.getenv("RESEND_API_KEY");

    private final HttpClient httpClient = HttpClient.newHttpClient();

    public boolean sendEmail(String toAddress, String subject, String htmlContent) {
        // Constructing a basic JSON payload for Resend
        String requestBody = String.format(
            "{ \"from\": \"notifications@sdmovers.com\", \"to\": [\"%s\"], \"subject\": \"%s\", \"html\": \"%s\" }",
            toAddress, subject, htmlContent.replace("\"", "\\\"")
        );

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(RESEND_API_URL))
                    .header("Authorization", "Bearer " + RESEND_API_KEY)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 200 || response.statusCode() == 201) {
                LOGGER.info("Email sent successfully to " + toAddress);
                return true;
            } else {
                LOGGER.warning("Failed to send email. HTTP Status: " + response.statusCode() + " Body: " + response.body());
            }
        } catch (Exception e) {
            LOGGER.severe("Error calling Resend API: " + e.getMessage());
        }
        return false;
    }
}