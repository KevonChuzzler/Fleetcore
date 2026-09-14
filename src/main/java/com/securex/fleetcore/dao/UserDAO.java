package com.securex.fleetcore.dao;

import com.securex.fleetcore.entity.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
@Transactional
public class UserDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(User user) {
        em.persist(user);
    }

    public User findById(Long id) {
        return em.find(User.class, id);
    }

    public List<User> findAll() {
        return em.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    public User update(User user) {
        return em.merge(user);
    }

    public void delete(Long id) {
        User user = findById(id);
        if (user != null) {
            em.remove(user);
        }
    }
}