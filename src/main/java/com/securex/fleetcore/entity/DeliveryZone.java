package com.securex.fleetcore.entity;

import jakarta.persistence.*;
import org.locationtech.jts.geom.Polygon;

@Entity
@Table(name = "delivery_zone")
public class DeliveryZone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "zone_id")
    private Long zoneId;

    @Column(name = "zone_name", unique = true)
    private String zoneName;

    @Column(columnDefinition = "geometry(Polygon,4326)")
    private Polygon boundaries; 
    
    public DeliveryZone() {}

    public Long getZoneId() { return zoneId; }
    public void setZoneId(Long zoneId) { this.zoneId = zoneId; }

    public String getZoneName() { return zoneName; }
    public void setZoneName(String zoneName) { this.zoneName = zoneName; }

    public Polygon getBoundaries() { return boundaries; }
    public void setBoundaries(Polygon boundaries) { this.boundaries = boundaries; }
}