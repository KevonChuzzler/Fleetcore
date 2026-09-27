# FleetCore Enterprise Management System

FleetCore is a Jakarta EE 10 enterprise logistics and fleet management platform. It provides real-time GPS telemetry, TSP-based route optimization, advanced dispatch workflows, and automated financial analytics.

## Technology Stack
* **Runtime:** WildFly (via `wildfly-maven-plugin`)
* **Framework:** Jakarta EE 10 (JAX-RS, JPA, CDI, WebSockets)
* **Language:** Java 21
* **Database:** PostgreSQL 15 + PostGIS (via Docker)
* **ORM:** Hibernate

---

## 1. Prerequisites
Ensure you have the following installed on your machine:
* **Java 21** (Eclipse Adoptium or similar)
* **Maven** (3.8+)
* **Docker Desktop** (For running the PostgreSQL + PostGIS database)
* **Git**

---

## 2. Quick Start Guide

### Step 1: Start the Database
The project includes a `docker-compose.yml` file pre-configured with PostgreSQL and the PostGIS extension required for geospatial routing.
```bash
docker-compose up -d