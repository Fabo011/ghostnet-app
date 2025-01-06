package com.shea.shepherd.repository;

import com.shea.shepherd.model.GhostNet;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;

public class GhostNetRepository {

    //@PersistenceContext
    private EntityManager entityManager;

    //@Transactional
    public void save(GhostNet ghostNet) {
        entityManager.persist(ghostNet);
    }

    public List<GhostNet> findAll() {
        return entityManager.createQuery("SELECT g FROM GhostNet g", GhostNet.class).getResultList();
    }
}
