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
}