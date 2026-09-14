package com.securex.fleetcore.dao;

import com.securex.fleetcore.entity.MaintenanceRecord;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
@Transactional
public class MaintenanceRecordDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(MaintenanceRecord record) {
        em.persist(record);
    }

    public MaintenanceRecord findById(Long id) {
        return em.find(MaintenanceRecord.class, id);
    }

    public List<MaintenanceRecord> findAll() {
        return em.createQuery("SELECT m FROM MaintenanceRecord m", MaintenanceRecord.class).getResultList();
    }

    public MaintenanceRecord update(MaintenanceRecord record) {
        return em.merge(record);
    }

    public void delete(Long id) {
        MaintenanceRecord record = findById(id);
        if (record != null) {
            em.remove(record);
        }
    }
}