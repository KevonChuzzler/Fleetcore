package com.securex.fleetcore.dao;

import com.securex.fleetcore.entity.VehicleAssignment;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
@Transactional
public class VehicleAssignmentDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(VehicleAssignment assignment) {
        em.persist(assignment);
    }

    public VehicleAssignment findById(Long id) {
        return em.find(VehicleAssignment.class, id);
    }

    public List<VehicleAssignment> findAll() {
        return em.createQuery("SELECT v FROM VehicleAssignment v", VehicleAssignment.class).getResultList();
    }

    public VehicleAssignment update(VehicleAssignment assignment) {
        return em.merge(assignment);
    }

    public void delete(Long id) {
        VehicleAssignment assignment = findById(id);
        if (assignment != null) {
            em.remove(assignment);
        }
    }
}