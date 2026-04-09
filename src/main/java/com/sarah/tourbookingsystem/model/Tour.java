package com.sarah.tourbookingsystem.model;

public class Tour {
    private String name;
    private String description;
    private double price;
    private int availableSeats;
    private double date;

    public Tour(String var1, String var2, double var3, int var4, double var5) {
        this.name = var1;
        this.description = var2;
        this.price = var3;
        this.availableSeats = var4;
        this.date = var5;
    }

    public String getDetails() {
        return this.name + " - " + this.description + " | $" + this.price + " | Date: " + this.date + " | Seats: " + this.availableSeats;
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

    public double getDate() {
        return this.date;
    }

    public void bookSeat() {
        --this.availableSeats;
    }

}