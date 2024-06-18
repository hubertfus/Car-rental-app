package com.example.carrentalapp;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Utility class for managing Hibernate session factory.
 */
public class HibernateUtil {
    // Hibernate session factory instance
    private static final SessionFactory sessionFactory = buildSessionFactory();

    /**
     * Builds the Hibernate session factory based on the configuration file.
     *
     * @return The built session factory.
     */
    private static SessionFactory buildSessionFactory() {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            return new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Retrieves the Hibernate session factory instance.
     *
     * @return The Hibernate session factory.
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * Shuts down the Hibernate session factory.
     * <p>
     * This method should be called when shutting down the application
     * to release all resources allocated for Hibernate.
     */
    public static void shutdown() {
        // Close caches and connection pools
        getSessionFactory().close();
    }
}
