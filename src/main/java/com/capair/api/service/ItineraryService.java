package com.capair.api.service;

import java.util.List;

import com.capair.api.model.Itinerary;
import com.capair.api.model.request.CheckInRequest;
import com.capair.api.model.response.BoardResponse;
import com.capair.api.model.response.CheckInResponse;
import com.capair.api.model.response.ItineraryResponse;
import com.capair.api.model.response.TripHolder;

public interface ItineraryService {
    List<ItineraryResponse> getItineraries();
    ItineraryResponse getItinerary(int id);
    CheckInResponse checkInFlight(CheckInRequest checkIn);
    TripHolder getTripInformation(Itinerary itinerary, int customerSize);
    List<BoardResponse> board(int itineraryId);
}
