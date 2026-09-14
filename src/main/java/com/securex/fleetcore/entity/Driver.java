package com.securex.fleetcore.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "DRIVER")
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "driver_id")
    private Long driverId; // Stores Driver ID[cite: 1]

    @NotNull
    @Column(name = "name")
    private String name; // Stores Name[cite: 1]

    @Column(name = "contact_information")
    private String contactInformation; // Stores Contact information[cite: 1]

    @NotNull
    @Column(name = "licence_number", unique = true)
    private String licenceNumber; // Stores Licence number[cite: 1]

    @Column(name = "licence_expiry_date")
    private LocalDate licenceExpiryDate; // Stores Licence expiry date[cite: 1]

    @Column(name = "driver_status")
    private String driverStatus; // Stores Driver status[cite: 1]

    // Default constructor required by JPA
    public Driver() {
    }

    // Getters and Setters
    public Long getDriverId() { return driverId; }
    public void setDriverId(Long driverId) { this.driverId = driverId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getContactInformation() { return contactInformation; }
    public void setContactInformation(String contactInformation) { this.contactInformation = contactInformation; }

    public String getLicenceNumber() { return licenceNumber; }
    public void setLicenceNumber(String licenceNumber) { this.licenceNumber = licenceNumber; }

    public LocalDate getLicenceExpiryDate() { return licenceExpiryDate; }
    public void setLicenceExpiryDate(LocalDate licenceExpiryDate) { this.licenceExpiryDate = licenceExpiryDate; }

    public String getDriverStatus() { return driverStatus; }
    public void setDriverStatus(String driverStatus) { this.driverStatus = driverStatus; }
}