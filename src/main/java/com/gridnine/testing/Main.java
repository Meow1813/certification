package com.gridnine.testing;

import com.gridnine.testing.builder.FlightBuilder;
import com.gridnine.testing.model.Flight;
import com.gridnine.testing.service.FlightFilter;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Flight> flights = FlightBuilder.createFlights();
        FlightFilter filteredFlights = new FlightFilter(flights);
        System.out.println("Список полетов");
        printFlights(flights);

        System.out.println("\nИсключение полетов с вылетом до текущего момента времени");
        printFlights(filteredFlights.departureBeforeCurrentTime().build());

        System.out.println("\nИсключение полетов с Сегментами с датой прилёта раньше даты вылета");
        printFlights(filteredFlights.arrivalDateBeforeDepartureDate().build());

        System.out.println("\nИсключение полетов с общим временем проведенным на земле больше двух часов");
        printFlights(filteredFlights.timeSpentOnEarthExceedsTwoHours().build());

    }
    public static void printFlights(List<Flight> flights){
        for (Flight flight:flights) {
            System.out.println(flight);
        }
    }
}