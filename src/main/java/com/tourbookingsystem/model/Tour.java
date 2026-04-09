package com.tourbookingsystem.model;

public class Tour {
    private String name;
    private String description;
    private double price;
    private int availableSeats;
    private double date;
    private String imageUrl;

    public Tour(String name, String description, double price, int availableSeats, double date) {
        this(name, description, price, availableSeats, date, "");
    }

    public Tour(String name, String description, double price, int availableSeats, double date, String imageUrl) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.availableSeats = availableSeats;
        this.date = date;
        this.imageUrl = imageUrl;
    }

    public String getDetails() {
        return this.name + " - " + this.description + " | $" + this.price + " | Date: " + this.date + " | Seats: " + this.availableSeats;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setDate(double date) {
        this.date = date;
    }

    public void setAvailableSeats(int seats) {
        this.availableSeats = seats;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void bookSeat() {
        --this.availableSeats;
    }

    public void releaseSeat() {
        ++this.availableSeats;
    }
}