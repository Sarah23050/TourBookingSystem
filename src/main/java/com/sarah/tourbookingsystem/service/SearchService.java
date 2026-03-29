package com.sarah.tourbookingsystem.service;

import com.sarah.tourbookingsystem.model.Tour;
import java.util.ArrayList;
import java.util.List;

public class SearchService {

    private List<Tour> tours = new ArrayList<>();

    public SearchService() {
        tours.add(new Tour("Bosphorus Tour", "Istanbul", 500, 20));
        tours.add(new Tour("Cappadocia Tour", "Nevşehir", 1500, 5));
        tours.add(new Tour("Antalya Beach Tour", "Antalya", 800, 7));
    }


    public List<Tour> searchTours(String keyword) {
        List<Tour> result = new ArrayList<>();

        for (Tour t : tours) {
            if (t.getName().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(t);
            }
        }

        return result;
    }




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
