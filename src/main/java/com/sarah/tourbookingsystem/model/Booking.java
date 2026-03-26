package com.sarah.tourbookingsystem.model;

public class Booking {
    private int bookingId;
    private String customerName;
    private String tourName;
    private int numberOfTickets;

    public Booking(int bookingId, String customerName, String tourName, int numberOfTickets) {
        this.bookingId = bookingId;
        this.customerName = customerName;
        this.tourName = tourName;
        this.numberOfTickets = numberOfTickets;
    }

    public int getBookingId() {
        return bookingId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getTourName() {
        return tourName;
    }

    public int getNumberOfTickets() {
        return numberOfTickets;
    }
}
