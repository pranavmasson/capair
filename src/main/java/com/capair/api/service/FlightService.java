package com.capair.api.service;

import java.time.LocalDateTime;
import java.util.List;

import com.capair.api.model.Flight;
import com.capair.api.model.request.BookingRequest;
import com.capair.api.model.request.FlightSearchRequest;
import com.capair.api.model.response.FlightResponse;
import com.capair.api.model.response.ItineraryResponse;
import com.capair.api.model.response.Trip;

public interface FlightService {
    List<FlightResponse> getFlights();
    FlightResponse getFlightResponse(String id);
    Flight getFlight(String id);
    ItineraryResponse bookFlight(BookingRequest bookRequest, String jwt);
    List<Trip> searchFlights(FlightSearchRequest searchRequest, LocalDateTime startTime);
}
