package com.sarah.tourbookingsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.List;

import com.sarah.tourbookingsystem.model.Tour;
import com.sarah.tourbookingsystem.service.SearchService;

@SpringBootApplication
public class TourBookingSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(TourBookingSystemApplication.class, args);

        SearchService searchService = new SearchService();

        System.out.println("Search Results:");
        List<Tour> results = searchService.searchTours("Tour");

        for (Tour t : results) {
            System.out.println(t.getName() + " - " + t.getLocation());
        }
    }

}
