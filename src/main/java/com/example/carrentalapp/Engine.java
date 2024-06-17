package com.example.carrentalapp;

import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Represents an engine entity used in cars.
 */
@Entity
@Table(name = "engine", schema = "car_rental")
public class Engine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idengine", nullable = false)
    private Integer id;

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "power")
    private Integer power;

    @Column(name = "fueltype", length = 50)
    private String fueltype;

    @OneToMany(mappedBy = "engine")
    private Set<Car> cars = new LinkedHashSet<>();

    /**
     * Constructs an Engine object with specified attributes.
     *
     * @param name     The name of the engine.
     * @param power    The power of the engine in horsepower.
     * @param fueltype The type of fuel the engine uses.
     */
    public Engine(String name, Integer power, String fueltype) {
        this.name = name;
        this.power = power;
        this.fueltype = fueltype;
    }

    /**
     * Default constructor for JPA.
     */
    public Engine() {
    }

    /**
     * Constructs an Engine object with specified attributes.
     *
     * @param id       The unique identifier of the engine.
     * @param name     The name of the engine.
     * @param power    The power of the engine in horsepower.
     * @param fueltype The type of fuel the engine uses.
     */
    public Engine(Integer id, String name, Integer power, String fueltype) {
        this.id = id;
        this.name = name;
        this.power = power;
        this.fueltype = fueltype;
    }

    /**
     * Retrieves the unique identifier of the engine.
     *
     * @return The ID of the engine.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the engine.
     *
     * @param id The ID to set.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Retrieves the name of the engine.
     *
     * @return The name of the engine.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the engine.
     *
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the power of the engine.
     *
     * @return The power of the engine in horsepower.
     */
    public Integer getPower() {
        return power;
    }

    /**
     * Sets the power of the engine.
     *
     * @param power The power to set in horsepower.
     */
    public void setPower(Integer power) {
        this.power = power;
    }

    /**
     * Retrieves the fuel type of the engine.
     *
     * @return The fuel type of the engine.
     */
    public String getFueltype() {
        return fueltype;
    }

    /**
     * Sets the fuel type of the engine.
     *
     * @param fueltype The fuel type to set.
     */
    public void setFueltype(String fueltype) {
        this.fueltype = fueltype;
    }

    /**
     * Retrieves the set of cars associated with this engine.
     *
     * @return The set of cars associated with this engine.
     */
    public Set<Car> getCars() {
        return cars;
    }

    /**
     * Sets the set of cars associated with this engine.
     *
     * @param cars The set of cars to associate with this engine.
     */
    public void setCars(Set<Car> cars) {
        this.cars = cars;
    }

    /**
     * Returns a string representation of the engine object.
     *
     * @return A string representation including ID, name, power, and fuel type.
     */
    @Override
    public String toString() {
        return getId() + " " + getName() + " " + getPower() + " " + getFueltype();
    }
}