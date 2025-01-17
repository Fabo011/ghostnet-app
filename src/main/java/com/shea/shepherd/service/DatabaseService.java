package com.shea.shepherd.service;

import com.shea.shepherd.model.GhostNetEntity;
import com.shea.shepherd.model.GhostNetStatus;
import com.shea.shepherd.model.UserEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;
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

    public List<GhostNetEntity> getAllGhostNetsSortedByStatus() {
        em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Query query = em.createQuery(
                    "SELECT g FROM GhostNetEntity g ORDER BY " +
                            "CASE g.status " +
                            "WHEN 'REPORTED' THEN 1 " +
                            "WHEN 'RECOVERY_PENDING' THEN 2 " +
                            "WHEN 'RECOVERED' THEN 3 " +
                            "WHEN 'MISSING' THEN 4 END",
                    GhostNetEntity.class
            );
            List<GhostNetEntity> ghostNets = query.getResultList();
            em.getTransaction().commit();
            LOGGER.info("Ghost nets fetched successfully: " + ghostNets.size() + " nets found.");
            return ghostNets;
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to fetch ghost nets: {0}", e.getMessage());
            return null;
        } finally {
            em.close(); // Closing the EntityManager after the operation
        }
    }

    public void assignRetrieverToGhostNet(Long ghostNetId, String retrieverUsername) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            GhostNetEntity ghostNet = em.find(GhostNetEntity.class, ghostNetId);
            if (ghostNet == null) {
                throw new Exception("GhostNet not found with ID: " + ghostNetId);
            }

            if (ghostNet.getAssignedUser() != null) {
                throw new Exception("This GhostNet already has an assigned retriever.");
            }

            // Assign the retriever username and update the status
            ghostNet.setAssignedUser(retrieverUsername);
            ghostNet.setStatus(GhostNetStatus.RECOVERY_PENDING);
            em.merge(ghostNet);

            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println("Error assigning retriever to GhostNet: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    public void assignMissingReporterToGhostNet(Long ghostNetId, String missingReporterUsername) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            GhostNetEntity ghostNet = em.find(GhostNetEntity.class, ghostNetId);
            if (ghostNet == null) {
                throw new Exception("GhostNet not found with ID: " + ghostNetId);
            }

            if (ghostNet.getMissingReporterName() != null) {
                throw new Exception("This GhostNet was already reported as missing.");
            }

            // Assign the retriever username and update the status
            ghostNet.setMissingReporterName(missingReporterUsername);
            ghostNet.setStatus(GhostNetStatus.MISSING);
            em.merge(ghostNet);

            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println("Error assigning retriever to GhostNet: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    public void updateGhostNetStatus(Long ghostNetId, GhostNetStatus newStatus) {
        em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            GhostNetEntity ghostNet = em.find(GhostNetEntity.class, ghostNetId);
            if (ghostNet == null) {
                throw new Exception("GhostNet not found.");
            }

            ghostNet.setStatus(newStatus);
            em.merge(ghostNet);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error updating GhostNet status: " + e.getMessage());
        } finally {
            em.close();
        }
    }
}