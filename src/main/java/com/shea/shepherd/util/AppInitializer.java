package com.shea.shepherd.util;

import io.github.cdimascio.dotenv.Dotenv;

public class AppInitializer {
    public static void init() {
        Dotenv dotenv = Dotenv.configure().load();

        // Set System Properties
        System.setProperty("DB_URL", "jdbc:postgresql://" + dotenv.get("DB_HOST") + ":" + dotenv.get("DB_PORT") + "/" + dotenv.get("DB_NAME"));
        System.setProperty("DB_USER", dotenv.get("DB_USER"));
        System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
    }
}
