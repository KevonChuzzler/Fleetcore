package com.securex.fleetcore.dao;

import com.securex.fleetcore.entity.Vehicle;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
@Transactional
public class VehicleDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(Vehicle vehicle) {
        em.persist(vehicle);
    }

    public Vehicle findById(Long id) {
        return em.find(Vehicle.class, id);
    }

    public List<Vehicle> findAll() {
        return em.createQuery("SELECT v FROM Vehicle v", Vehicle.class).getResultList();
    }

    public Vehicle update(Vehicle vehicle) {
        return em.merge(vehicle);
    }

    public void delete(Long id) {
        Vehicle vehicle = findById(id);
        if (vehicle != null) {
            em.remove(vehicle);
        }
    }
}