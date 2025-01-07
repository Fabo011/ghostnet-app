package com.shea.shepherd;

import com.shea.shepherd.db.DatabaseConnection;
import com.shea.shepherd.db.HibernateSchemaInitializer;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("startUp")
@ApplicationScoped
public class AppStartupBean {
    @Inject
    private String start = "startupMessage";

    public String startUpApp() {
        System.out.println("Initializing application...");

        // Initialize the database connection
        DatabaseConnection.checkDatabaseConnection();

        // Initialize Hibernate schema
        HibernateSchemaInitializer.initializeSchema();

        System.out.println("Application initialized successfully.");

        return start;
    }
}


