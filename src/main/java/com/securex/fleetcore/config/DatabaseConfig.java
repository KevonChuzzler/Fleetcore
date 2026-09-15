package com.securex.fleetcore.config;

import jakarta.annotation.sql.DataSourceDefinition;
import jakarta.enterprise.context.ApplicationScoped;

@DataSourceDefinition(
    name = "java:global/jdbc/FleetcoreDS",
    className = "org.postgresql.ds.PGSimpleDataSource",
    url = "jdbc:postgresql://localhost:5432/fleetcore",
    user = "postgres",
    password = "securepassword"
)
@ApplicationScoped
public class DatabaseConfig {
    // This class tells WildFly to automatically build a PostgreSQL connection 
    // pool linking to your Docker container when the app deploys.
}