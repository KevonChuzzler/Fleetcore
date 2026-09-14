package com.securex.fleetcore.dao;

import com.securex.fleetcore.entity.MaintenanceSchedule;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
@Transactional
public class MaintenanceScheduleDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(MaintenanceSchedule schedule) {
        em.persist(schedule);
    }

    public MaintenanceSchedule findById(Long id) {
        return em.find(MaintenanceSchedule.class, id);
    }

    public List<MaintenanceSchedule> findAll() {
        return em.createQuery("SELECT m FROM MaintenanceSchedule m", MaintenanceSchedule.class).getResultList();
    }

    public MaintenanceSchedule update(MaintenanceSchedule schedule) {
        return em.merge(schedule);
    }

    public void delete(Long id) {
        MaintenanceSchedule schedule = findById(id);
        if (schedule != null) {
            em.remove(schedule);
        }
    }
}