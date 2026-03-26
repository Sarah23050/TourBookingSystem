package com.sarah.tourbookingsystem.model;

public class Tour {
    private String name;
    private String location;
    private double price;
    private String date;

    public Tour(String name, String location, double price, String date) {
        this.name = name;
        this.location = location;
        this.price = price;
        this.date = date;
    }

    public String getName() { return name; }
    public String getLocation() { return location; }
    public double getPrice() { return price; }
    public String getDate() { return date; }
}
