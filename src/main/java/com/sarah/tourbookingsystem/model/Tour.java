package com.sarah.tourbookingsystem.model;

public class Tour {
    private String name;
    private String description;
    private double price;
    private int availableSeats;

    public Tour(String var1, String var2, double var3, int var5) {
        this.name = var1;
        this.description = var2;
        this.price = var3;
        this.availableSeats = var5;
    }

    public String getDetails() {
        return this.name + " - " + this.description + " | $" + this.price + " | Seats: " + this.availableSeats;
    }

    public String getName() {
        return this.name;
    }

    public double getPrice() {
        return this.price;
    }

    public int getAvailableSeats() {
        return this.availableSeats;
    }

    public void bookSeat() {
        --this.availableSeats;
    }

}