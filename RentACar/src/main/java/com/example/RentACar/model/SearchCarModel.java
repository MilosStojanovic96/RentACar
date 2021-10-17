package com.example.RentACar.model;

import java.util.UUID;

public class SearchCarModel {
    private Integer year;
    private String make;
    private String model;
    private Boolean automatic;
    private Double price;
    private Integer power;
    private Integer doors;
    public SearchCarModel(int year, String make, String model,
                          boolean automatic, double price, int power, int doors) {
        this.year = year;
        this.make = make;
        this.model = model;
        this.automatic = automatic;
        this.price = price;
        this.power = power;
        this.doors = doors;
    }
    public Integer getYear() {
        return year;
    }
    public String getMake() {
        return make;
    }
    public String getModel() {
        return model;
    }
    public Double getPrice() {
        return price;
    }
    public Integer getPower() {
        return power;
    }
    public Integer getDoors() {
        return doors;
    }
    public Boolean getAutomatic() {
        return automatic;
    }
}
