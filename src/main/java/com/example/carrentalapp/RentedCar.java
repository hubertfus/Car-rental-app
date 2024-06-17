package com.example.carrentalapp;

import jakarta.persistence.*;

import java.time.LocalDate;

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

    public RentedCar(){}

    public RentedCar(Client client, Car car, String rentedDate, String rentedFrom, String rentedUntil) {
        this.client = client;
        this.car = car;
        this.rentedDate = rentedDate;
        this.rentedFrom = rentedFrom;
        this.rentedUntil = rentedUntil;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client idclient) {
        this.client = idclient;
    }

    public String getRentedDate() {
        return rentedDate;
    }

    public void setRentedDate(String rentedDate) {
        this.rentedDate = rentedDate;
    }

    public String getRentedFrom() {
        return rentedFrom;
    }

    public void setRentedFrom(String rentedFrom) {
        this.rentedFrom = rentedFrom;
    }

    public String getRentedUntil() {
        return rentedUntil;
    }

    public void setRentedUntil(String rentedUntil) {
        this.rentedUntil = rentedUntil;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }


}