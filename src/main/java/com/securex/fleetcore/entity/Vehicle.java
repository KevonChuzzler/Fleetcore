package com.securex.fleetcore.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;

@Entity
@Table(name = "vehicles")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String registrationNumber;
    private String make;
    private String model;
    private String vehicleType;
    private Integer year;
    private Double mileage;
    private String status;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL)
    private List<TrackingEvent> trackingEvents;

    // --- Getters and Setters ---

    public Long getId() { 
        return id; 
    }
    
    public void setId(Long id) { 
        this.id = id; 
    }

    public String getRegistrationNumber() { 
        return registrationNumber; 
    }
    
    public void setRegistrationNumber(String registrationNumber) { 
        this.registrationNumber = registrationNumber; 
    }

    public String getMake() { 
        return make; 
    }
    
    public void setMake(String make) { 
        this.make = make; 
    }

    public String getModel() { 
        return model; 
    }
    
    public void setModel(String model) { 
        this.model = model; 
    }

    public String getVehicleType() { 
        return vehicleType; 
    }
    
    public void setVehicleType(String vehicleType) { 
        this.vehicleType = vehicleType; 
    }

    public Integer getYear() { 
        return year; 
    }
    
    public void setYear(Integer year) { 
        this.year = year; 
    }

    public Double getMileage() { 
        return mileage; 
    }
    
    public void setMileage(Double mileage) { 
        this.mileage = mileage; 
    }

    public String getStatus() { 
        return status; 
    }
    
    public void setStatus(String status) { 
        this.status = status; 
    }

    public List<TrackingEvent> getTrackingEvents() { 
        return trackingEvents; 
    }
    
    public void setTrackingEvents(List<TrackingEvent> trackingEvents) { 
        this.trackingEvents = trackingEvents; 
    }
}