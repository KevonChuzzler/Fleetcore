package com.securex.fleetcore.config;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/api")
public class RestApplication extends Application {
    // This class remains empty. The @ApplicationPath annotation triggers the server to scan for your controllers.
}