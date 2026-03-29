package com.sarah.tourbookingsystem;

import com.sarah.tourbookingsystem.main.BookingSystem;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.List;

import com.sarah.tourbookingsystem.model.*;
import com.sarah.tourbookingsystem.service.SearchService;

@SpringBootApplication
public class TourBookingSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(TourBookingSystemApplication.class, args);

        SearchService searchService = new SearchService();

        System.out.println("Search Results:");
        List<Tour> results = searchService.searchTours("Tour");

        System.out.println("Starting Tour Booking System...");
        BookingSystem var1 = new BookingSystem();
        var1.start();
    }

}
