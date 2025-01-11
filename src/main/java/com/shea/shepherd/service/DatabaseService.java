package com.shea.shepherd.service;

import com.shea.shepherd.model.GhostNetEntity;
import com.shea.shepherd.model.GhostNetStatus;
import com.shea.shepherd.model.UserEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import org.mindrot.jbcrypt.BCrypt;

import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseService {
    private static final Logger LOGGER = Logger.getLogger(DatabaseService.class.getName());
    private EntityManagerFactory emf;
    private EntityManager em;

    // Initialize EntityManagerFactory once
    public DatabaseService() {
        emf = Persistence.createEntityManagerFactory("ghostnet");
    }

    // Check if a user already exists by username
    public UserEntity findUserByUsername(String username) {
        em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT u FROM UserEntity u WHERE u.name = :username", UserEntity.class);
            query.setParameter("username", username);
            UserEntity user = (UserEntity) query.getSingleResult();
            em.getTransaction().commit();
            LOGGER.info("User found: " + user.getName());
            return user;
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "User not found for username: {0}", username);
            return null;
        } finally {
            em.close(); // Closing the EntityManager only after operation is done
        }
    }

    public void createUser(String username, String role, String phoneNumber, String password) {
        em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            UserEntity user = new UserEntity();
            user.setName(username);
            user.setRole(role);
            user.setPhoneNumber(phoneNumber);
            user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
            em.persist(user);
            em.getTransaction().commit();
            LOGGER.info("User created successfully.");
        } catch (Exception e) {
            em.getTransaction().rollback();
            LOGGER.log(Level.SEVERE, "Error creating user: {0}", e.getMessage());
        } finally {
            em.close(); // Closing the EntityManager only after operation is done
        }
    }

    public void saveGhostNetReport(String username, String location, String size) {
        em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            GhostNetEntity ghostNet = new GhostNetEntity();
            ghostNet.setReporterUsername(username);
            ghostNet.setLocation(location);
            ghostNet.setSize(size);
            ghostNet.setStatus(GhostNetStatus.REPORTED);
            em.persist(ghostNet);
            em.getTransaction().commit();
            LOGGER.info("Ghost net reported successfully.");
        } catch (Exception e) {
            em.getTransaction().rollback();
            LOGGER.log(Level.SEVERE, "Error reporting ghost net: {0}", e.getMessage());
        } finally {
            em.close(); // Closing the EntityManager only after operation is done
        }
    }

    // Close EntityManagerFactory only when the application shuts down
    public void closeEntityManagerFactory() {
        if (emf != null) {
            emf.close();  // Close EntityManagerFactory only at shutdown
            LOGGER.info("EntityManagerFactory closed.");
        }
    }
}



