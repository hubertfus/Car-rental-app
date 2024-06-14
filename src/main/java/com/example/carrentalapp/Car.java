package com.example.carrentalapp;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "cars", schema = "car_rental")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcar", nullable = false)
    private Integer id;

    @Column(name = "brand", length = 50)
    private String brand;

    @Column(name = "model", length = 50)
    private String model;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idengine")
    private Engine idengine;

    @Column(name = "price", precision = 9, scale = 2)
    private BigDecimal price;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Engine getIdengine() {
        return idengine;
    }

    public void setIdengine(Engine idengine) {
        this.idengine = idengine;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

}