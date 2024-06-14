package com.example.carrentalapp;

import jakarta.persistence.*;

import java.time.Instant;

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
    private Instant rentedDate;

    @Column(name = "rented_from")
    private Instant rentedFrom;

    @Column(name = "rented_until")
    private Instant rentedUntil;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Client getIdclient() {
        return client;
    }

    public void setIdclient(Client idclient) {
        this.client = idclient;
    }

    public Instant getRentedDate() {
        return rentedDate;
    }

    public void setRentedDate(Instant rentedDate) {
        this.rentedDate = rentedDate;
    }

    public Instant getRentedFrom() {
        return rentedFrom;
    }

    public void setRentedFrom(Instant rentedFrom) {
        this.rentedFrom = rentedFrom;
    }

    public Instant getRentedUntil() {
        return rentedUntil;
    }

    public void setRentedUntil(Instant rentedUntil) {
        this.rentedUntil = rentedUntil;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

}