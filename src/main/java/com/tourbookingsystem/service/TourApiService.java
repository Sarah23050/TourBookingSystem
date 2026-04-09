package com.tourbookingsystem.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TourApiService {

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private static final Pattern DOWNLOAD_URL_PATTERN = Pattern.compile("\\\"download_url\\\"\\s*:\\s*\\\"(.*?)\\\"");

    public List<String> fetchTourImageUrls(int limit) {
        List<String> imageUrls = new ArrayList<>();

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://picsum.photos/v2/list?page=1&limit=" + Math.max(limit, 6)))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                Matcher matcher = DOWNLOAD_URL_PATTERN.matcher(response.body());
                while (matcher.find()) {
                    imageUrls.add(matcher.group(1));
                }
            }
        } catch (Exception ignored) {
            // Use URL placeholders below if API is unavailable.
        }

        List<String> placeholders = getPlaceholderImageUrls();
        while (imageUrls.size() < limit) {
            imageUrls.add(placeholders.get(imageUrls.size() % placeholders.size()));
        }

        return imageUrls;
    }

    private List<String> getPlaceholderImageUrls() {
        List<String> placeholders = new ArrayList<>();
        placeholders.add("https://picsum.photos/seed/tour-sea/900/600"); //image placeholder
        placeholders.add("https://picsum.photos/seed/tour-mountain/900/600"); //image placeholder
        placeholders.add("https://picsum.photos/seed/tour-city/900/600"); //image placeholder
        placeholders.add("https://picsum.photos/seed/tour-desert/900/600"); //image placeholder
        placeholders.add("https://picsum.photos/seed/tour-island/900/600"); //image placeholder
        placeholders.add("https://picsum.photos/seed/tour-lake/900/600"); //image placeholder
        return placeholders;
    }
}