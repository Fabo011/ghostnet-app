package com.shea.shepherd.service;

import com.shea.shepherd.model.AppUser;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.mindrot.jbcrypt.BCrypt;

@ApplicationScoped
public class UserService {

    @PersistenceContext
    private EntityManager entityManager;

    // Authenticate user by comparing hashed passwords or create a new user if not found
    @Transactional
    public AppUser authenticate(String username, String password, String role) {
        try {
            // Attempt to find the user in the database
            AppUser user = entityManager.createQuery(
                            "SELECT u FROM 'appUser' u WHERE u.name = :name", AppUser.class)
                    .setParameter("name", username)
                    .getSingleResult();

            // If the user is found, check the password
            if (BCrypt.checkpw(password, user.getPasswordHash())) {
                return user;
            } else {
                return null; // Password mismatch
            }
        } catch (Exception e) {
            // If the user is not found, create a new one automatically with selected role
            AppUser newUser = new AppUser();
            newUser.setName(username);
            newUser.setRole(role); // Set the role chosen by the user

            // Hash the password before saving
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            newUser.setPasswordHash(hashedPassword);

            // Persist the new user to the database
            entityManager.persist(newUser);

            return newUser; // Return the newly registered user
        }
    }

    // Register a new user with selected role
    public void registerUser(AppUser user, String plainPassword, String role) {
        // Hash the plain password before saving to the database
        String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
        user.setPasswordHash(hashedPassword);
        user.setRole(role); // Assign the selected role

        // Save the user entity with the hashed password
        entityManager.persist(user);
    }
}
