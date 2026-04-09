package com.tourbookingsystem.service;

import com.tourbookingsystem.model.Tour;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TourService {

    public List<Tour> loadToursFromFile() {
        List<Tour> tours = new ArrayList<>();
        File file = new File("tours.txt");

        if (!file.exists()) {
            return tours;
        }

        try (Scanner reader = new Scanner(file)) {
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String[] parts = line.split("\\|", 6);

                if (parts.length >= 5) {
                    try {
                        String name = parts[0];
                        String description = parts[1];
                        double price = Double.parseDouble(parts[2]);
                        int seats = Integer.parseInt(parts[3]);
                        double date = Double.parseDouble(parts[4]);
                        String imageUrl = parts.length >= 6 ? parts[5] : "";

                        tours.add(new Tour(name, description, price, seats, date, imageUrl));
                    } catch (NumberFormatException ignored) {
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tours;
    }

    public void saveToursToFile(List<Tour> tours) {
        try (PrintWriter writer = new PrintWriter("tours.txt")) {
            for (Tour tour : tours) {
                writer.println(tour.getName() + "|" +
                        tour.getDescription() + "|" +
                        tour.getPrice() + "|" +
                        tour.getAvailableSeats() + "|" +
                        tour.getDate() + "|" +
                        (tour.getImageUrl() == null ? "" : tour.getImageUrl()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
