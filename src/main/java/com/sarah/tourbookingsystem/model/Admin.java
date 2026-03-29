package com.sarah.tourbookingsystem.model;

import com.sarah.tourbookingsystem.model.Tour;
import com.sarah.tourbookingsystem.model.Customer;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Admin extends User {
    private List<Tour> allTours = new ArrayList();
    private List<Customer> allCustomers = new ArrayList();

    public Admin(String var1, String var2) {
        super(var1, var2);
        this.loadSampleTours();
    }

    private void loadSampleTours() {
        this.allTours.add(new Tour("Paris Adventure", "Visit Eiffel Tower", (double)500.0F, 10));
        this.allTours.add(new Tour("Beach Relax", "7 days beach vacation", (double)300.0F, 15));
        this.allTours.add(new Tour("Mountain Trek", "Hiking adventure", (double)400.0F, 8));
    }

    public void showAllBookings() {
        System.out.println("\n\ud83d\udcca All Customer Bookings:");

        for(Customer var2 : this.allCustomers) {
            System.out.println("\n\ud83d\udc64 " + var2.getUsername() + ":");
            var2.showBookedTours();
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
        this.allTours.add(new Tour(var2, var3, var4, var6));
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