package com.example.carrentalapp;

import org.hibernate.Session;

/**
 * Class responsible for reading client details from the database.
 */
public class ReadClient {

    /**
     * Main method to start the application.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        readClient(1L); // Reads the client with ID 1.
    }

    /**
     * Reads and prints the details of a client based on the provided user ID.
     *
     * @param userId The ID of the user to be read from the database.
     */
    public static void readClient(Long userId) {
        Session session = HibernateUtil.getSessionFactory().openSession(); // Opens a new session.

        try {
            Car client = session.get(Car.class, userId); // Retrieves the client details.
            if (client != null) {
                // Prints out the brand and model of the car if the client is found.
                System.out.println("User details: " + client.getBrand() + ", " + client.getModel());
            } else {
                // Prints out a message if the client is not found.
                System.out.println("User not found!");
            }
        } catch (Exception e) {
            e.printStackTrace(); // Prints out an exception if there's an error.
        } finally {
            session.close(); // Closes the session.
        }
    }
}

