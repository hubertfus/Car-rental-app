package com.example.carrentalapp;

import org.hibernate.Session;

public class ReadClient {
    public static void main(String[] args) {
        readClient(1L);
    }
    public static void readClient(Long userId) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            Client client = session.get(Client.class, userId);
            if (client != null) {
                System.out.println("User details: " + client.getFirstname() + ", " + client.getEmail());
            } else {
                System.out.println("User not found!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}

