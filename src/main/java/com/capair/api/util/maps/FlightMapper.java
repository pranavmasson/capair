package com.capair.api.util.maps;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.capair.api.model.Flight;
import com.capair.api.model.response.FlightResponse;
import com.capair.api.model.response.GroupTicketResponse;

@Mapper(componentModel = "spring")
public interface FlightMapper {
    
    @Mapping(target="arrivalTime", source="scheduledArvTime")
    @Mapping(target="departureTime", source="departureTime")
    @Mapping(target="destinationAirport", source="destinationAirport")
    @Mapping(target="sourceAirport", source="sourceAirport")
    @Mapping(target="econSeatsAvailable", source="numEconomySeats")
    @Mapping(target="busSeatsAvailable", source="numBusinessSeats")
    @Mapping(target="airplaneType", source="airplaneType")
    @Mapping(target="flightID", source="flightId")
    FlightResponse flightToFlightResponse(Flight flight);
    List<FlightResponse> flightToFlightResponse(List<Flight> flights);

    @Mapping(target="ticketIds", ignore=true)
    @Mapping(target="seatSection", ignore=true)
    @Mapping(target="checkedIn", ignore=true)
    @Mapping(target="checkInOpen", ignore=true)
    @Mapping(target="flightID", source="flightId")
    @Mapping(target="arrivalTime", source="scheduledArvTime") 
    @Mapping(target="econSeatsAvailable", source="numEconomySeats")
    @Mapping(target="busSeatsAvailable", source="numBusinessSeats")
    @Mapping(target="airplaneType", source="airplaneType")
    GroupTicketResponse flightToGroupTicketResponse(Flight flight);
}

