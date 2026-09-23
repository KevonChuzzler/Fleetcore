# FleetCore - Comprehensive Setup & Installation Guide

Welcome to **FleetCore**, a Jakarta EE fleet management system backed by PostgreSQL and powered by WildFly. This document provides step-by-step instructions to set up, build, run, and test the project on any machine, regardless of your choice of IDE.

---

## Table of Contents
1. [Prerequisites](#1-prerequisites)
2. [Cloning the Repository](#2-cloning-the-repository)
3. [Starting the Database (Docker)](#3-starting-the-database-docker)
4. [Opening the Project in Your IDE](#4-opening-the-project-in-your-ide)
5. [Running the Backend Server (WildFly)](#5-running-the-backend-server-wildfly)
6. [API Endpoints & Verification](#6-api-endpoints--verification)
7. [Security Configuration (Google OAuth2)](#7-security-configuration-google-oauth2)
8. [Troubleshooting](#8-troubleshooting)

---

### 1. Prerequisites

Before getting started, ensure you have the following installed and configured on your machine:
* **Java Development Kit (JDK 17 or higher)**: Ensure `JAVA_HOME` is set up correctly in your environment variables.
* **Apache Maven (3.8+)**: Ensure `mvn` is accessible via your terminal command line.
* **Docker & Docker Compose**: Ensure Docker Desktop is running in the background.
* **Git**: To clone the repository.

---

### 2. Cloning the Repository

Open your terminal or command prompt and clone the repository to your local machine:

```bash
git clone [https://github.com/YOUR_USERNAME/fleetcore.git](https://github.com/YOUR_USERNAME/fleetcore.git)
cd fleetcore

---

### 3. Starting the Database (Docker)

FleetCore uses Docker Compose to run a local PostgreSQL database instance. This container auto-provisions your database and sets up your tables (`VEHICLE`, `DRIVER`, `TRACKING_EVENT`, `VEHICLE_ASSIGNMENT`) via Hibernate JPA auto-generation rules.

*   Open your terminal in the root directory of the project (where `docker-compose.yml` is located).
*   Start the database container in the background:

```bash
docker-compose up -d
```

*   Verify that the container is active:

```bash
docker ps
```

### 4. Opening the Project in Your IDE

FleetCore is structured as a standard Maven project and can be opened in any major IDE:

**Option A: IntelliJ IDEA**
*   Open IntelliJ IDEA and select **File > Open**.
*   Navigate to and select the cloned `fleetcore` folder.
*   IntelliJ will automatically recognize the `pom.xml` file. Accept any prompts to import or trust the Maven project.

**Option B: VS Code**
*   Open VS Code and go to **File > Open Folder...**, then select the `fleetcore` folder.
*   Make sure you have the **Extension Pack for Java** and **Maven for Java** extensions installed.
*   VS Code will auto-resolve dependencies from the `pom.xml`.

**Option C: Eclipse**
*   Go to **File > Import...**
*   Select **Maven > Existing Maven Projects** and browse to the `fleetcore` directory.
*   Click **Finish** to import the project workspace.

### 5. Running the Backend Server (WildFly)

This project utilizes the WildFly Maven plugin to build and launch a local development server with hot-reloading capabilities.

*   Open your terminal inside the project root folder (or use your IDE's built-in terminal).
*   Run the WildFly development command:

```bash
mvn wildfly:dev
```

Wait for Maven to download necessary dependencies (on your first run) and start the server. Once the console logs indicate that WildFly has successfully started, your backend environment is live.

### 6. API Endpoints & Verification

Your local backend is hosted at:
`http://localhost:8080/fleetcore-1.0.0-SNAPSHOT/api/`

**Core Endpoints:**
*   Tracking Data: `GET /api/tracking`
*   Vehicle Assignments:
    *   `GET /api/assignments` — View all vehicle-driver links.
    *   `POST /api/assignments` — Link a driver to a vehicle.

**Testing Examples via PowerShell:**

Assign a Driver to a Vehicle (POST):
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/fleetcore-1.0.0-SNAPSHOT/api/assignments" -Method Post -ContentType "application/json" -Body '{"vehicle":{"vehicleId":1},"driver":{"driverId":1}}'
```

Fetch All Assignments (GET):
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/fleetcore-1.0.0-SNAPSHOT/api/assignments"
```

### 7. Security Configuration (Google OAuth2)

FleetCore features a security filter (`AuthenticationFilter.java`) designed to verify Google OAuth2 tokens.

*   **Local Development (Bypassed):** To allow easy local endpoint testing without needing valid Google JWT tokens, the security filter is temporarily bypassed by commenting out the `@Provider` annotation inside `AuthenticationFilter.java`:

```java
// @Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter { ... }
```

*   **Production Deployment:** To fully lock down the application and enforce token verification, remove the comment slashes to re-enable `@Provider`.

### 8. Troubleshooting

*   **Port Conflicts (5432 or 8080):** Ensure no other local PostgreSQL server is occupying port 5432, and no other Java/WildFly process is using port 8080 before launching.
*   **Database Connection Errors:** Verify that your Docker container is actively running via `docker ps` and matches the database configurations outlined in your `persistence.xml`.