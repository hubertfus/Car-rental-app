package com.example.carrentalapp;

import jakarta.persistence.*;

/**
 * Entity class representing a rented car record in the database.
 */
@Entity
@Table(name = "rented_car", schema = "car_rental")
public class RentedCar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idrent", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idclient")
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idcar")
    private Car car;

    @Column(name = "rented_date")
    private String rentedDate;

    @Column(name = "rented_from")
    private String rentedFrom;

    @Column(name = "rented_until")
    private String rentedUntil;

    /**
     * Default constructor required by JPA.
     */
    public RentedCar() {
    }

    /**
     * Constructor to initialize a rented car with specified client, car, and rental dates.
     *
     * @param client      The client who rented the car.
     * @param car         The car that was rented.
     * @param rentedDate  The date when the car was rented.
     * @param rentedFrom  The starting date of the rental period.
     * @param rentedUntil The ending date of the rental period.
     */
    public RentedCar(Client client, Car car, String rentedDate, String rentedFrom, String rentedUntil) {
        this.client = client;
        this.car = car;
        this.rentedDate = rentedDate;
        this.rentedFrom = rentedFrom;
        this.rentedUntil = rentedUntil;
    }

    /**
     * Retrieves the ID of the rented car.
     *
     * @return The ID of the rented car.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the ID of the rented car.
     *
     * @param id The ID to set.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Retrieves the client who rented the car.
     *
     * @return The client who rented the car.
     */
    public Client getClient() {
        return client;
    }

    /**
     * Sets the client who rented the car.
     *
     * @param client The client to set.
     */
    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * Retrieves the date when the car was rented.
     *
     * @return The rented date.
     */
    public String getRentedDate() {
        return rentedDate;
    }

    /**
     * Sets the date when the car was rented.
     *
     * @param rentedDate The rented date to set.
     */
    public void setRentedDate(String rentedDate) {
        this.rentedDate = rentedDate;
    }

    /**
     * Retrieves the starting date of the rental period.
     *
     * @return The starting date of the rental period.
     */
    public String getRentedFrom() {
        return rentedFrom;
    }

    /**
     * Sets the starting date of the rental period.
     *
     * @param rentedFrom The starting date to set.
     */
    public void setRentedFrom(String rentedFrom) {
        this.rentedFrom = rentedFrom;
    }

    /**
     * Retrieves the ending date of the rental period.
     *
     * @return The ending date of the rental period.
     */
    public String getRentedUntil() {
        return rentedUntil;
    }

    /**
     * Sets the ending date of the rental period.
     *
     * @param rentedUntil The ending date to set.
     */
    public void setRentedUntil(String rentedUntil) {
        this.rentedUntil = rentedUntil;
    }

    /**
     * Retrieves the car that was rented.
     *
     * @return The rented car.
     */
    public Car getCar() {
        return car;
    }

    /**
     * Sets the car that was rented.
     *
     * @param car The car to set.
     */
    public void setCar(Car car) {
        this.car = car;
    }
}