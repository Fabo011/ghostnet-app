import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.EntityManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestDatabaseConnection {
    public static void main(String[] args) {

        String url = "jdbc:postgresql://localhost:5432/ghostnetdatabase";
        String user = "ghostnet";
        String password = "test";

        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connection successful!");
            connection.close();
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }

        try {
            // Create an EntityManagerFactory using the persistence unit defined in persistence.xml
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("ghostnet");
            EntityManager em = emf.createEntityManager();

            // Try a simple operation to test if everything works
            em.getTransaction().begin();
            em.createQuery("SELECT 1").getSingleResult();
            em.getTransaction().commit();

            System.out.println("Persistence.xml is being executed and schema generation should have occurred!");

            em.close();
            emf.close();

        } catch (Exception e) {
            System.out.println("Error during persistence initialization: " + e.getMessage());
        }
    }
}


