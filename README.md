# FleetCore - Fleet Management System

FleetCore is a comprehensive, cloud-based fleet management system developed by SecureX for the logistics and transportation company, SDMovers[cite: 1]. The system centralizes fleet information and operations to assist with vehicle tracking, maintenance management, fuel monitoring, driver management, and reporting[cite: 1]. 

## Key Features

* **Vehicle Tracking and GPS Monitoring:** Utilizes external GPS data to allow administrators to monitor the real-time location and status of fleet vehicles[cite: 1].
* **Route Optimisation:** Identifies efficient delivery routes based on distance, traffic, and delivery requirements to reduce travel time and fuel consumption[cite: 1].
* **Maintenance Scheduling:** Tracks vehicle maintenance schedules and triggers automated alerts for upcoming or overdue servicing to minimize vehicle downtime[cite: 1].
* **Fuel Management:** Logs vehicle mileage alongside fuel quantities and costs to calculate consumption and monitor fuel-related expenses[cite: 1].
* **Driver Management:** Maintains driver records, tracks assigned vehicles, evaluates driving habits, and alerts management when important driver documents approach their expiry dates[cite: 1].
* **User Access Control:** Enforces Role-Based Access Control (RBAC) via Google OAuth to ensure data is securely restricted according to user roles (e.g., Administrator, Fleet Manager, Driver)[cite: 1].
* **Reporting and Analytics:** Generates actionable statistics, tables, and charts relating to overall fleet performance[cite: 1].

## System Architecture

FleetCore is built on a cloud-based microservices architecture, where independent services handle specific business functions[cite: 1]. Communication between the web interface and the backend microservices is managed by a central API Gateway[cite: 1]. The system is designed to provide 99.9% yearly uptime and to scale seamlessly from 25 vehicles to hundreds of vehicles[cite: 1].

## Technology Stack

| Component | Technology Used |
| :--- | :--- |
| **Programming Language** | Java[cite: 1] |
| **Enterprise Framework** | Jakarta EE (RESTful Web Services, JPA, Security, Validation)[cite: 1] |
| **Database** | PostgreSQL or MySQL[cite: 1] |
| **Data Format & Communication** | JSON over REST/HTTPS, WebSockets for real-time updates[cite: 1] |
| **Cloud Hosting** | Google Kubernetes Engine (GKE), Cloud Pub/Sub, BigQuery[cite: 1] |
| **Containerisation** | Docker[cite: 1] |

## External API Integrations

The system leverages several external services to power its core features:
* **Tracker Data Hub (Tracker Data Services REST API):** Ingests live vehicle GPS coordinates and status updates[cite: 1].
* **Google Maps Platform Route Optimization API (GMPRO) & Google Routes API:** Powers mapping, traffic monitoring, and route planning capabilities[cite: 1].
* **FNB Integration Channel:** Tracks service-related and fuel transactions[cite: 1].
* **Resend:** Dispatches email alerts and notifications to users[cite: 1].

## Deployment & Environments

Deployment is managed through a complete CI/CD pipeline supporting automated builds, version control, and multi-stage testing[cite: 1]. 
The pipeline targets three main environments:
1. **Development:** Used locally by the SecureX engineering team[cite: 1].
2. **Testing/Staging:** Used to execute unit, integration, and end-to-end testing before final release[cite: 1].
3. **Production:** The live environment utilized by SDMovers[cite: 1].

## Team Secure X

**Lecturer:** Dr B Nomvete[cite: 1]
**Group Members:** 
* Mokoena Michelle [cite: 1]
* Mndebele Asande [cite: 1]
* Thobejane Bridget [cite: 1]
* Shabangu Sfiso [cite: 1]
* Sindani Senzo [cite: 1]
* Ngobeni Wisani [cite: 1]