package com.sarah.tourbookingsystem.service;

import com.sarah.tourbookingsystem.model.Booking;

import java.util.ArrayList;
import java.util.List;

public class BookingService {

    private List<Booking> bookings = new ArrayList<>();

    public void addBooking(Booking booking) {
        bookings.add(booking);
    }

    public void cancelBooking(int bookingId) {
        bookings.removeIf(b -> b.getBookingId() == bookingId);
    }

    public List<Booking> getAllBookings() {
        return bookings;
    }

    public void saveBookingsToFile() {
        try {
            java.io.PrintWriter writer = new java.io.PrintWriter("bookings.txt");

            for (Booking b : bookings) {
                writer.println(b.getBookingId() + "," +
                        b.getCustomerName() + "," +
                        b.getTourName() + "," +
                        b.getNumberOfTickets());
            }

            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadBookingsFromFile() {
        try {
            java.io.File file = new java.io.File("bookings.txt");

            if (!file.exists()) return;

            java.util.Scanner reader = new java.util.Scanner(file);

            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String[] parts = line.split(",");

                Booking b = new Booking(
                        Integer.parseInt(parts[0]),
                        parts[1],
                        parts[2],
                        Integer.parseInt(parts[3])
                );

                bookings.add(b);
            }

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}