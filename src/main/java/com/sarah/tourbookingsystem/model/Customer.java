package com.sarah.tourbookingsystem.model;

import java.util.ArrayList;
import java.util.List;

public class Customer extends User {
    private List<Booking> bookings = new ArrayList<>();

    public Customer(String var1, String var2) {
        super(var1, var2);
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void bookTour(Tour tour, int seats) {
        Booking booking = new Booking(
                (int)(Math.random() * 1000),
                this.username,
                tour.getName(),
                seats
        );

        bookings.add(booking);

        System.out.println("✅ Booked " + seats + " seats for " + tour.getName());
    }

    public void cancelBooking(int index) {
        if (index >= 0 && index < bookings.size()) {
            System.out.println("❌ Cancelled: " + bookings.get(index).getTourName());
            bookings.remove(index);
        } else {
            System.out.println("❌ Invalid selection!");
        }
    }

    public void showBookedTours() {
        if (bookings.isEmpty()) {
            System.out.println("No bookings.");
        } else {
            for (int i = 0; i < bookings.size(); i++) {
                Booking b = bookings.get(i);
                System.out.println((i+1) + ". " +
                        b.getTourName() + " | Seats: " + b.getNumberOfTickets());
            }
        }
    }


}