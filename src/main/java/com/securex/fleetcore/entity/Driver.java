package com.securex.fleetcore.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "drivers")
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String licenseNumber;
    private LocalDate licenseExpiryDate;
    private String contactNumber;
    private String status; // e.g., ACTIVE, INACTIVE, ON_LEAVE

    // --- Getters and Setters ---

    public Long getId() { 
        return id; 
    }
    
    public void setId(Long id) { 
        this.id = id; 
    }

    public String getFirstName() { 
        return firstName; 
    }
    
    public void setFirstName(String firstName) { 
        this.firstName = firstName; 
    }

    public String getLastName() { 
        return lastName; 
    }
    
    public void setLastName(String lastName) { 
        this.lastName = lastName; 
    }

    public String getLicenseNumber() { 
        return licenseNumber; 
    }
    
    public void setLicenseNumber(String licenseNumber) { 
        this.licenseNumber = licenseNumber; 
    }

    public LocalDate getLicenseExpiryDate() { 
        return licenseExpiryDate; 
    }
    
    public void setLicenseExpiryDate(LocalDate licenseExpiryDate) { 
        this.licenseExpiryDate = licenseExpiryDate; 
    }

    public String getContactNumber() { 
        return contactNumber; 
    }
    
    public void setContactNumber(String contactNumber) { 
        this.contactNumber = contactNumber; 
    }

    public String getStatus() { 
        return status; 
    }
    
    public void setStatus(String status) { 
        this.status = status; 
    }
}