package com.securex.fleetcore.dao;

import com.securex.fleetcore.entity.Notification;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
@Transactional
public class NotificationDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(Notification notification) {
        em.persist(notification);
    }

    public Notification findById(Long id) {
        return em.find(Notification.class, id);
    }

    public List<Notification> findAll() {
        return em.createQuery("SELECT n FROM Notification n", Notification.class).getResultList();
    }

    public Notification update(Notification notification) {
        return em.merge(notification);
    }

    public void delete(Long id) {
        Notification notification = findById(id);
        if (notification != null) {
            em.remove(notification);
        }
    }
}