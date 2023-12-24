package com.gridnine.testing.service;

import com.gridnine.testing.model.Flight;
import com.gridnine.testing.model.Segment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FlightFilterTest {
    LocalDateTime threeDaysFromNow = LocalDateTime.now().plusDays(3);
    LocalDateTime threeDaysBeforeNow = LocalDateTime.now().minusDays(3);
    static List<Flight> flightListWithFlightsBeforeCurrentTime;
    static List<Flight> normalFlightList;
    static List<Flight> flightListWithArrivalDateBeforeDepartureDate;
    static List<Flight> flightListWithTimeSpentOnEarthExceedsTwoHours;
    static List<Flight> allFlights;
    static List<Flight> flightListWithoutNormalFlights;

    public void initFlightListWithFlightsBeforeCurrentTime(){
        flightListWithFlightsBeforeCurrentTime = new ArrayList<>();

        List<Segment> firstFlightSegments = new ArrayList<>();
        firstFlightSegments.add(new Segment(threeDaysBeforeNow,threeDaysBeforeNow.plusHours(3)));
        flightListWithFlightsBeforeCurrentTime.add(new Flight(firstFlightSegments));

        List<Segment> secondFlightSegments = new ArrayList<>();
        secondFlightSegments.add(new Segment(threeDaysBeforeNow.plusHours(1),threeDaysBeforeNow.plusHours(4)));
        secondFlightSegments.add(new Segment(threeDaysBeforeNow.plusHours(5),threeDaysBeforeNow.plusHours(9)));
        flightListWithFlightsBeforeCurrentTime.add(new Flight(secondFlightSegments));
    }

    public void initNormalFlightList(){
        normalFlightList = new ArrayList<>();

        List<Segment> firstFlightSegments = new ArrayList<>();
        firstFlightSegments.add(new Segment(threeDaysFromNow,threeDaysFromNow.plusHours(2)));
        normalFlightList.add(new Flight(firstFlightSegments));

        List<Segment> secondFlightSegments = new ArrayList<>();
        secondFlightSegments.add(new Segment(threeDaysFromNow.plusHours(1),threeDaysFromNow.plusHours(4)));
        secondFlightSegments.add(new Segment(threeDaysFromNow.plusHours(5),threeDaysFromNow.plusHours(9)));
        normalFlightList.add(new Flight(secondFlightSegments));
    }

    public void initFlightListWithArrivalDateBeforeDepartureDate(){
        flightListWithArrivalDateBeforeDepartureDate = new ArrayList<>();

        List<Segment> firstFlightSegments = new ArrayList<>();
        firstFlightSegments.add(new Segment(threeDaysFromNow,threeDaysBeforeNow.plusHours(2)));
        flightListWithArrivalDateBeforeDepartureDate.add(new Flight(firstFlightSegments));

        List<Segment> secondFlightSegments = new ArrayList<>();
        secondFlightSegments.add(new Segment(threeDaysFromNow.plusHours(1),threeDaysFromNow.plusHours(4)));
        secondFlightSegments.add(new Segment(threeDaysFromNow.plusHours(5),threeDaysFromNow.minusHours(9)));
        flightListWithArrivalDateBeforeDepartureDate.add(new Flight(secondFlightSegments));
    }

    public void initFlightListWithTimeSpentOnEarthExceedsTwoHours(){
        flightListWithTimeSpentOnEarthExceedsTwoHours = new ArrayList<>();

        List<Segment> firstFlightSegments = new ArrayList<>();
        firstFlightSegments.add(new Segment(threeDaysFromNow,threeDaysFromNow.plusHours(2)));
        firstFlightSegments.add(new Segment(threeDaysFromNow.plusHours(5),threeDaysFromNow.plusHours(7)));
        flightListWithTimeSpentOnEarthExceedsTwoHours.add(new Flight(firstFlightSegments));

        List<Segment> secondFlightSegments = new ArrayList<>();
        secondFlightSegments.add(new Segment(threeDaysFromNow.plusHours(1),threeDaysFromNow.plusHours(4)));
        secondFlightSegments.add(new Segment(threeDaysFromNow.plusHours(8),threeDaysFromNow.plusHours(9)));
        flightListWithTimeSpentOnEarthExceedsTwoHours.add(new Flight(secondFlightSegments));
    }

    @BeforeEach
    public void initFlightLists(){
        allFlights = new ArrayList<>();
        flightListWithoutNormalFlights = new ArrayList<>();

        initNormalFlightList();
        initFlightListWithFlightsBeforeCurrentTime();
        initFlightListWithArrivalDateBeforeDepartureDate();
        initFlightListWithTimeSpentOnEarthExceedsTwoHours();

        allFlights.addAll(normalFlightList);
        allFlights.addAll(flightListWithFlightsBeforeCurrentTime);
        allFlights.addAll(flightListWithArrivalDateBeforeDepartureDate);
        allFlights.addAll(flightListWithTimeSpentOnEarthExceedsTwoHours);

        flightListWithoutNormalFlights.addAll(flightListWithFlightsBeforeCurrentTime);
        flightListWithoutNormalFlights.addAll(flightListWithArrivalDateBeforeDepartureDate);
        flightListWithoutNormalFlights.addAll(flightListWithTimeSpentOnEarthExceedsTwoHours);
    }

    @Test
    void shouldReturnFlightsWithoutFlightWithDepartureBeforeCurrentTime() {
        List<Flight> expectedFlight = new ArrayList<>();
        expectedFlight.addAll(normalFlightList);
        expectedFlight.addAll(flightListWithArrivalDateBeforeDepartureDate);
        expectedFlight.addAll(flightListWithTimeSpentOnEarthExceedsTwoHours);

        FlightFilter filter = new FlightFilter(allFlights);

        List<Flight> filteredFlight = filter
                .departureBeforeCurrentTime()
                .build();

        assertEquals(expectedFlight,filteredFlight);
    }

    @Test
    void shouldReturnFlightsWithoutFlightWithArrivalDateBeforeDepartureDate() {
        List<Flight> expectedFlight = new ArrayList<>();
        expectedFlight.addAll(normalFlightList);
        expectedFlight.addAll(flightListWithFlightsBeforeCurrentTime);
        expectedFlight.addAll(flightListWithTimeSpentOnEarthExceedsTwoHours);

        FlightFilter filter = new FlightFilter(allFlights);

        List<Flight> filteredFlight = filter
                .arrivalDateBeforeDepartureDate()
                .build();

        assertEquals(expectedFlight,filteredFlight);
    }

    @Test
    void shouldReturnFlightsWithoutFlightWithTimeSpentOnEarthExceedsTwoHours() {
        List<Flight> expectedFlight = new ArrayList<>();
        expectedFlight.addAll(normalFlightList);
        expectedFlight.addAll(flightListWithFlightsBeforeCurrentTime);
        expectedFlight.addAll(flightListWithArrivalDateBeforeDepartureDate);

        FlightFilter filter = new FlightFilter(allFlights);

        List<Flight> filteredFlight = filter
                .timeSpentOnEarthExceedsTwoHours()
                .build();

        assertEquals(expectedFlight,filteredFlight);
    }

    @Test
    void shouldReturnNormalFLightsAfterAllFilters() {

        FlightFilter filter = new FlightFilter(allFlights);

        List<Flight> filteredFlight = filter
                .timeSpentOnEarthExceedsTwoHours()
                .departureBeforeCurrentTime()
                .arrivalDateBeforeDepartureDate()
                .build();

        assertEquals(normalFlightList,filteredFlight);
    }

    @Test
    void shouldReturnEmptyListAfterAllFilters() {
        FlightFilter filter = new FlightFilter(flightListWithoutNormalFlights);

        List<Flight> filteredFlight = filter
                .timeSpentOnEarthExceedsTwoHours()
                .departureBeforeCurrentTime()
                .arrivalDateBeforeDepartureDate()
                .build();

        assertTrue(filteredFlight.isEmpty());
    }

    @Test
    void shouldReturnEmptyListAfterFilteringEmptyList() {
        FlightFilter filter = new FlightFilter(new ArrayList<>());

        List<Flight> filteredFlight = filter
                .timeSpentOnEarthExceedsTwoHours()
                .departureBeforeCurrentTime()
                .arrivalDateBeforeDepartureDate()
                .build();

        assertTrue(filteredFlight.isEmpty());
    }
}