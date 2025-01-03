package com.shea.shepherd.db;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.EntityManager;

public class HibernateSchemaInitializer {

    public static void initializeSchema() {
        try {
            // Create an EntityManagerFactory to initialize Hibernate and generate the schema
            EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("ghostnetPU");
            EntityManager entityManager = entityManagerFactory.createEntityManager();

            // This ensures Hibernate processes the schema creation/update
            entityManager.getTransaction().begin();
            entityManager.getTransaction().commit();  // Commit the transaction, which can also trigger schema creation
            entityManager.close();
            entityManagerFactory.close();

            System.out.println("Hibernate Schema Generation Process Completed.");
        } catch (Exception e) {
            System.err.println("Failed to initialize the schema.");
            e.printStackTrace();
        }
    }
}
