package com.sarah.tourbookingsystem.service;

import com.sarah.tourbookingsystem.model.Tour;
import java.util.ArrayList;
import java.util.List;

public class SearchService {

    public List<Tour> searchTours(List<Tour> tours, String keyword) {
        List<Tour> result = new ArrayList<>();

        for (Tour t : tours) {
            if (t.getName().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(t);
            }
        }

        return result;
    }

    public List<Tour> filterByPrice(List<Tour> tours, double maxPrice) {
        List<Tour> result = new ArrayList<>();

        for (Tour t : tours) {
            if (t.getPrice() <= maxPrice) {
                result.add(t);
            }
        }

        return result;
    }
}