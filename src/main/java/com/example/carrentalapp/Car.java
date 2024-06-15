package com.example.carrentalapp;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "cars", schema = "car_rental")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcar")
    private Integer id;

    @Column(name = "brand", length = 50, nullable = false)
    private String brand;

    @Column(name = "model", length = 50, nullable = false)
    private String model;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idengine", nullable = false)
    private Engine engine;

    @Column(name = "price", precision = 9, scale = 2, nullable = false)
    private BigDecimal price;

    // No-arg constructor required by JPA
    public Car() {
    }

    public Car(String brand, String model, Engine engine, BigDecimal price) {
        this.brand = brand;
        this.model = model;
        this.engine = engine;
        this.price = price;
    }

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

    public Engine getEngine() {
        return engine;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
