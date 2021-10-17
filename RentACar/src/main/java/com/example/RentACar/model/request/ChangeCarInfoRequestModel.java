package com.example.RentACar.model.request;

public class ChangeCarInfoRequestModel {
    private String licence_plate;
    private String make;
    private String model;
    private int year;
    private int engine_capacity;
    private String color;
    private double price;
    private int doors;
    private String size;
    private int power;
    private boolean automatic;
    private String fuel;
    private String image;
    public ChangeCarInfoRequestModel(String licence_plate, String make, String model, int year, int engine_capacity,
                                     String color, double price, int doors, String size, int power, boolean automatic, String fuel, String image) {
        this.licence_plate = licence_plate;
        this.make = make;
        this.model = model;
        this.year = year;
        this.engine_capacity = engine_capacity;
        this.color = color;
        this.price = price;
        this.doors = doors;
        this.size = size;
        this.power = power;
        this.automatic = automatic;
        this.fuel = fuel;
        this.image = image;
    }
    public String getLicence_plate() {
        return licence_plate;
    }
    public String getMake() {
        return make;
    }
    public String getModel() {
        return model;
    }
    public int getYear() {
        return year;
    }
    public int getEngine_capacity() {
        return engine_capacity;
    }
    public String getColor() {
        return color;
    }
    public double getPrice() {
        return price;
    }
    public int getDoors() {
        return doors;
    }
    public String getSize() {
        return size;
    }
    public int getPower() {
        return power;
    }
    public boolean isAutomatic() {
        return automatic;
    }
    public String getFuel() {
        return fuel;
    }
    public String getImage() {
        return image;
    }
}
