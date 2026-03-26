package com.sarah.tourbookingsystem.service;

import com.sarah.tourbookingsystem.model.Tour;
import java.util.ArrayList;
import java.util.List;

public class SearchService {

    private List<Tour> tours = new ArrayList<>();

    // Add sample tours
    public SearchService() {
        tours.add(new Tour("Bosphorus Tour", "Istanbul", 500, "2026-05-01"));
        tours.add(new Tour("Cappadocia Tour", "Nevşehir", 1500, "2026-06-10"));
        tours.add(new Tour("Antalya Beach Tour", "Antalya", 800, "2026-07-15"));
    }

    // SEARCH
    public List<Tour> searchTours(String keyword) {
        List<Tour> result = new ArrayList<>();

        for (Tour t : tours) {
            if (t.getName().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(t);
            }
        }

        return result;
    }

    // FILTER BY LOCATION
    public List<Tour> filterByLocation(String location) {
        List<Tour> result = new ArrayList<>();

        for (Tour t : tours) {
            if (t.getLocation().equalsIgnoreCase(location)) {
                result.add(t);
            }
        }

        return result;
    }

    // FILTER BY PRICE
    public List<Tour> filterByPrice(double maxPrice) {
        List<Tour> result = new ArrayList<>();

        for (Tour t : tours) {
            if (t.getPrice() <= maxPrice) {
                result.add(t);
            }
        }

        return result;
    }
}
