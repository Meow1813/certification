package com.gridnine.testing.service;

import com.gridnine.testing.model.Flight;
import com.gridnine.testing.model.Segment;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class FlightFilter {
    List<Flight> flights;

    public FlightFilter(List<Flight> flights) {
        this.flights = new ArrayList<>(flights);
    }

    public List<Flight> build() {
        return flights;
    }

    public FlightFilter departureBeforeCurrentTime() {
        List<Flight> filteredFlights = flights.stream()
                .filter(flight -> {
                    LocalDateTime depDay = flight.getSegments().get(0).getDepartureDate();
                    return depDay.isAfter(LocalDateTime.now());
                })
                .toList();
        return new FlightFilter(filteredFlights);
    }

    public FlightFilter arrivalDateBeforeDepartureDate() {
        List<Flight> filteredFlights = flights.stream()
                .filter(flight ->
                        flight.getSegments().stream()
                                .allMatch(segment -> segment.getDepartureDate().isBefore(segment.getArrivalDate())))
                .toList();

        return new FlightFilter(filteredFlights);
    }

    public FlightFilter timeSpentOnEarthExceedsTwoHours() {
        List<Flight> filteredFlights = flights.stream()
                .filter(flight -> {
                    List<Segment> segments = flight.getSegments();
                    long totalDuration = IntStream.range(1, segments.size())
                            .mapToLong(i -> Duration.between(segments.get(i - 1).getArrivalDate(), segments.get(i).getDepartureDate()).toHours())
                            .sum();
                    return totalDuration < 2;
                })
                .toList();
        return new FlightFilter(filteredFlights);
    }
}
