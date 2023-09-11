package com.capair.api.util.maps;

import java.time.LocalDateTime;
import java.util.List;

import org.mapstruct.Mapper;

import com.capair.api.model.response.FlightResponse;
import com.capair.api.model.response.Trip;

@Mapper(componentModel = "spring")
public abstract class TripMapper {
    
    public Trip flightResponsetoTrip(List<FlightResponse> flights, int totalEconomyCost, int totalBusinessCost, LocalDateTime latestFlight){
        if (flights == null){
            return null;
        }

        Trip trip = new Trip();

        trip.setFlightCount(flights.size());
        trip.setFlights(flights);
        trip.setTotalEconomyCost(totalEconomyCost);
        trip.setTotalBusinessCost(totalBusinessCost);
        trip.setLatestFlight(latestFlight);

        return trip;
    }
}
