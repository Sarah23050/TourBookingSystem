package com.tourbookingsystem.model;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Admin extends User {
    private List<Tour> allTours = new ArrayList<>();
    private List<Customer> allCustomers = new ArrayList<>();

    public Admin(String var1, String var2) {
        super(var1, var2);
    }

    public List<Customer> getAllCustomers() {
        return allCustomers;
    }

    public void loadSampleTours() {
        this.allTours.add(new Tour(
            "Sahara Desert Adventure-Morocco",
            "3 days Experience the magic of the Sahara with camel rides, stargazing, and traditional Berber camps.",
            299.0,
            15,
            15.5,
            "https://static.vecteezy.com/system/resources/thumbnails/044/458/543/small/camels-in-the-sahara-desert-morocco-africa-photo.jpg"
        ));
        this.allTours.add(new Tour(
            "Bali Ruins Trail-Indonesia",
            "Discover ancient temples, rice terraces, and hidden waterfalls in the heart of Bali.",
            199.0,
            20,
            15.5,
            "https://media.istockphoto.com/id/653953140/photo/hindu-temple-in-bali.jpg?s=612x612&w=0&k=20&c=ysj3S2kV1ZgCr4QZWDzjvHRowCI3-cR1xQNnqE8-BS4="
        ));
        this.allTours.add(new Tour(
            "Northern Lights Expedition-Iceland",
            "Chase the aurora borealis across the Arctic wilderness with expert guides.",
            499.0,
            12,
            2.2,
            "https://media.istockphoto.com/id/614127332/photo/aurora-borealis.jpg?s=612x612&w=0&k=20&c=BXs-xClKQwAVPKGqjvO5LcmnE2Q73vwvhPgxtJvqEcg="
        ));
        this.allTours.add(new Tour(
            "Amalfi Coast Sailing-Italy",
            "3 days Sail along the stunning Amalfi Coast, stopping at charming villages and secret coves.",
            379.0,
            10,
            6.6,
            "https://media.istockphoto.com/id/992943278/photo/amalfitan-coast-with-cruise-liner.jpg?s=612x612&w=0&k=20&c=fssn1I6EYChqEedZXW0a8a3DM4AEkIvYbQouAfqF4Y0="
        ));
        this.allTours.add(new Tour(
            "Machu Picchu Trek-Peru",
            "Hike the legendary Inca Trail to the lost city of Machu Picchu.",
            449.0,
            16,
            5.5,
            "https://images.pexels.com/photos/18662534/pexels-photo-18662534/free-photo-of-machu-picchu-by-sunrise.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"
        ));
        this.allTours.add(new Tour(
            "Tokyo Street Food Tour-Japan",
            "Explore hidden alleyways and taste the best street food Tokyo has to offer.",
            89.0,
            12,
            8.8,
            "https://media.istockphoto.com/id/1071391480/photo/osaka-shinsekai-at-night-tsutenkaku-tower.jpg?s=612x612&w=0&k=20&c=RrqZwqjYD0OUogqYbR5L4Q45xzqPkkVFkNjEpW0yT64="
        ));
    }

    public void addTour(Tour tour) {
        this.allTours.add(tour);
    }

    public void showAllBookings() {
        System.out.println("\n\ud83d\udcca All Customer Bookings:");

        for(Customer var2 : this.allCustomers) {
            System.out.println("\n\ud83d\udc64 " + var2.getUsername() + ":");
            for (Booking b : var2.getBookings()) {
                System.out.println(
                        "Tour: " + b.getTourName() +
                                " | Seats: " + b.getNumberOfTickets()
                );
            }
        }

    }

    public void addTour(Scanner var1) {
        System.out.print("Tour name: ");
        String var2 = var1.nextLine();
        System.out.print("Description: ");
        String var3 = var1.nextLine();
        System.out.print("Price: $");
        double var4 = var1.nextDouble();
        var1.nextLine();
        System.out.print("Available seats: ");
        int var6 = var1.nextInt();
        var1.nextLine();
        System.out.println("Date: ");
        int var7 = var1.nextInt();
        var1.nextLine();
        this.allTours.add(new Tour(var2, var3, var4, var6, var7));
        System.out.println("✅ New tour added!");
    }

    public void deleteTour(int var1) {
        if (var1 >= 0 && var1 < this.allTours.size()) {
            PrintStream var10000 = System.out;
            Object var10001 = this.allTours.get(var1);
            var10000.println("\ud83d\uddd1️ Deleted: " + ((Tour)var10001).getName());
            this.allTours.remove(var1);
        } else {
            System.out.println("❌ Invalid tour number!");
        }

    }

    public void showAllTours() {
        System.out.println("\n\ud83c\udfde️ Available Tours:");

        for(int var1 = 0; var1 < this.allTours.size(); ++var1) {
            System.out.println(var1 + 1 + ". " + ((Tour)this.allTours.get(var1)).getDetails());
        }

    }

    public List<Tour> getAllTours() {
        return this.allTours;
    }

    public void addCustomer(Customer var1) {
        this.allCustomers.add(var1);
    }
}