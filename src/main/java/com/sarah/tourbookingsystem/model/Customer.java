package com.sarah.tourbookingsystem.model;

import java.util.ArrayList;
import java.util.List;

public class Customer extends User {
    private List<Tour> bookedTours = new ArrayList();

    public Customer(String var1, String var2) {
        super(var1, var2);
    }

    public void bookTour(Tour var1) {
        this.bookedTours.add(var1);
        System.out.println("✅ Tour booked: " + var1.getName());
    }

    public void showBookedTours() {
        if (this.bookedTours.isEmpty()) {
            System.out.println("No tours booked yet.");
        } else {
            System.out.println("\n\ud83d\udccb Your Booked Tours:");

            for(int var1 = 0; var1 < this.bookedTours.size(); ++var1) {
                System.out.println(var1 + 1 + ". " + ((Tour)this.bookedTours.get(var1)).getDetails());
            }
        }

    }

    public List<Tour> getBookedTours() {
        return this.bookedTours;
    }
}