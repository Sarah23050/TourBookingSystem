package com.sarah.tourbookingsystem.main;

import com.sarah.tourbookingsystem.model.*;

public class TourBookingSystem {
    public TourBookingSystem() {
    }

    public static void main(String[] args) {
        System.out.println("Starting Tour Booking System...");
        BookingSystem var1 = new BookingSystem();
        var1.start();
    }
}