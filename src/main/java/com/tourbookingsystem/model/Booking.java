package com.tourbookingsystem.model;

import java.time.LocalDate;

public class Booking {
    private int bookingId;
    private String customerName;
    private String tourName;
    private int numberOfTickets;
    private LocalDate bookingDate;

    public Booking(int bookingId, String customerName, String tourName, int numberOfTickets) {
        this(bookingId, customerName, tourName, numberOfTickets, LocalDate.now());
    }

    public Booking(int bookingId, String customerName, String tourName, int numberOfTickets, LocalDate bookingDate) {
        this.bookingId = bookingId;
        this.customerName = customerName;
        this.tourName = tourName;
        this.numberOfTickets = numberOfTickets;
        this.bookingDate = bookingDate;
    }

    public int getBookingId() {
        return bookingId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getTourName() {
        return tourName;
    }

    public int getNumberOfTickets() {
        return numberOfTickets;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }
}
