package com.shea.shepherd;

import com.shea.shepherd.util.AppInitializer;
import com.shea.shepherd.db.DatabaseConnection;
import com.shea.shepherd.db.HibernateSchemaInitializer;


public class Main {
    public static void main(String[] args) {
        // Initialize environment variables from .env file
        AppInitializer.init();

        DatabaseConnection.checkDatabaseConnection();
        HibernateSchemaInitializer.initializeSchema();
    }
}
