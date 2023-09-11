package com.scaler.cabbooking.services;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class CalculatorService {
    private static final int EARTH_RADIUS = 6371;
    public double calculateDistance(double sourceLatitude, double sourceLongitude, double destLatitude, double destLongitude) {
        double sourceLatRadians = Math.toRadians(sourceLatitude);
        double sourceLonRadians = Math.toRadians(sourceLongitude);
        double destLatRadians = Math.toRadians(destLatitude);
        double destLonRadians = Math.toRadians(destLongitude);

        // Calculate the difference between latitudes and longitudes
        double dLat = destLatRadians - sourceLatRadians;
        double dLon = destLonRadians - sourceLonRadians;

        // Haversine formula
        double a = Math.pow(Math.sin(dLat / 2), 2) +
                Math.cos(sourceLatRadians) * Math.cos(destLatRadians) *
                        Math.pow(Math.sin(dLon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // Calculate the distance
        double distance = EARTH_RADIUS * c;

        return distance;
    }

    public long calculateDuration(LocalDateTime startTime, LocalDateTime endTime) {
        Duration duration = Duration.between(startTime, endTime);
        return duration.getSeconds();
    }

    public double calculateFare(double distance) {
        double baseFare = 11;
        double totalFare = baseFare * distance;
        return totalFare;
    }
}
