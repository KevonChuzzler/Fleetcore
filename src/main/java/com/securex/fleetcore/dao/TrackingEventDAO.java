package com.securex.fleetcore.dao;

import com.securex.fleetcore.entity.TrackingEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
@Transactional
public class TrackingEventDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(TrackingEvent event) {
        em.persist(event);
    }

    public TrackingEvent findById(Long id) {
        return em.find(TrackingEvent.class, id);
    }

    public List<TrackingEvent> findAll() {
        return em.createQuery("SELECT t FROM TrackingEvent t", TrackingEvent.class).getResultList();
    }

    public TrackingEvent update(TrackingEvent event) {
        return em.merge(event);
    }

    public void delete(Long id) {
        TrackingEvent event = findById(id);
        if (event != null) {
            em.remove(event);
        }
    }
}