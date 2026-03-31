package com.sarah.tourbookingsystem;

import com.sarah.tourbookingsystem.main.BookingSystem;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.sarah.tourbookingsystem.model.*;

@SpringBootApplication
public class TourBookingSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(TourBookingSystemApplication.class, args);

        System.out.println("Starting Tour Booking System...");
        BookingSystem var1 = new BookingSystem();
        var1.start();
    }

}
