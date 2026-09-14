package com.securex.fleetcore.dao;

import com.securex.fleetcore.entity.Driver;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
@Transactional
public class DriverDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(Driver driver) {
        em.persist(driver);
    }

    public Driver findById(Long id) {
        return em.find(Driver.class, id);
    }

    public List<Driver> findAll() {
        return em.createQuery("SELECT d FROM Driver d", Driver.class).getResultList();
    }

    public Driver update(Driver driver) {
        return em.merge(driver);
    }

    public void delete(Long id) {
        Driver driver = findById(id);
        if (driver != null) {
            em.remove(driver);
        }
    }
}