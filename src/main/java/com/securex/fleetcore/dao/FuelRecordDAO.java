package com.securex.fleetcore.dao;

import com.securex.fleetcore.entity.FuelRecord;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
@Transactional
public class FuelRecordDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(FuelRecord fuelRecord) {
        em.persist(fuelRecord);
    }

    public FuelRecord findById(Long id) {
        return em.find(FuelRecord.class, id);
    }

    public List<FuelRecord> findAll() {
        return em.createQuery("SELECT f FROM FuelRecord f", FuelRecord.class).getResultList();
    }

    public FuelRecord update(FuelRecord fuelRecord) {
        return em.merge(fuelRecord);
    }

    public void delete(Long id) {
        FuelRecord fuelRecord = findById(id);
        if (fuelRecord != null) {
            em.remove(fuelRecord);
        }
    }
}