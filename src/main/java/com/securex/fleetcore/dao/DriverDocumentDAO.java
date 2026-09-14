package com.securex.fleetcore.dao;

import com.securex.fleetcore.entity.DriverDocument;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
@Transactional
public class DriverDocumentDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(DriverDocument document) {
        em.persist(document);
    }

    public DriverDocument findById(Long id) {
        return em.find(DriverDocument.class, id);
    }

    public List<DriverDocument> findAll() {
        return em.createQuery("SELECT d FROM DriverDocument d", DriverDocument.class).getResultList();
    }

    public DriverDocument update(DriverDocument document) {
        return em.merge(document);
    }

    public void delete(Long id) {
        DriverDocument document = findById(id);
        if (document != null) {
            em.remove(document);
        }
    }
}