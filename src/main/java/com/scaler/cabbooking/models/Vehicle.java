package com.scaler.cabbooking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.scaler.cabbooking.helper.VehicleType;
import jakarta.persistence.*;

@Entity
public class Vehicle {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(unique = true, name = "vehicleNumberPlate")
    private String vehicleNumberPlate;

    @Column(name = "make")
    private String make;

    @Column(name = "model")
    private String model;

    @Column(name = "makeYear")
    private String makeYear;

    @Column(name = "color")
    private String color;

    @Column(name = "type")
    private VehicleType vehicleType;

    @Column(name = "capacity")
    private Integer capacity;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    private Driver driver;

    public Integer getId() {
        return id;
    }

    public String getVehicleNumberPlate() {
        return vehicleNumberPlate;
    }

    public void setVehicleNumberPlate(String vehicleNumberPlate) {
        this.vehicleNumberPlate = vehicleNumberPlate;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getMakeYear() {
        return makeYear;
    }

    public void setMakeYear(String makeYear) {
        this.makeYear = makeYear;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }
}
