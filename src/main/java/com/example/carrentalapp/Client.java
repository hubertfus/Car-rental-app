package com.example.carrentalapp;

import jakarta.persistence.*;

/**
 * Entity class representing a client in the car rental application.
 */
@Entity
@Table(name = "clients", schema = "car_rental")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idclient", nullable = false)
    private Integer id; // Unique identifier for the client.

    @Column(name = "firstname", length = 50)
    private String firstname; // First name of the client.

    @Column(name = "lastname", length = 50)
    private String lastname; // Last name of the client.

    @Column(name = "email", length = 100)
    private String email; // Email address of the client.

    /**
     * Default constructor for JPA.
     */
    public Client() {

    }

    /**
     * Constructs a new Client with the given details.
     *
     * @param firstname The first name of the client.
     * @param lastname  The last name of the client.
     * @param email     The email address of the client.
     */
    public Client(String firstname, String lastname, String email) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
    }

    /**
     * Gets the unique identifier for this client.
     *
     * @return The unique identifier for this client.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the unique identifier for this client.
     *
     * @param id The unique identifier to set.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the first name of this client.
     *
     * @return The first name of this client.
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * Sets the first name of this client.
     *
     * @param firstname The first name to set.
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     * Gets the last name of this client.
     *
     * @return The last name of this client.
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * Sets the last name of this client.
     *
     * @param lastname The last name to set.
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    /**
     * Gets the email address of this client.
     *
     * @return The email address of this client.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of this client.
     *
     * @param email The email address to set.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return getId() + " " + getFirstname() + " " + getLastname() + " " + getEmail();
    }
}