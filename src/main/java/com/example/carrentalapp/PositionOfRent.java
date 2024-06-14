package com.example.carrentalapp;

import jakarta.persistence.*;

@Entity
@Table(name = "position_of_rents", schema = "car_rental")
public class PositionOfRent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idposition_of_rents", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idclient")
    private Client idclient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idrent")
    private RentedCar idrent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idcar")
    private Car idcar;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Client getIdclient() {
        return idclient;
    }

    public void setIdclient(Client idclient) {
        this.idclient = idclient;
    }

    public RentedCar getIdrent() {
        return idrent;
    }

    public void setIdrent(RentedCar idrent) {
        this.idrent = idrent;
    }

    public Car getIdcar() {
        return idcar;
    }

    public void setIdcar(Car idcar) {
        this.idcar = idcar;
    }

}