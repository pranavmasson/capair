package com.capair.api.util.maps;

import java.util.List;

import org.mapstruct.Mapper;

import com.capair.api.model.Airport;
import com.capair.api.model.response.AirportResponse;

@Mapper(componentModel = "spring")
public interface AirportMapper {
    
    AirportResponse airportToAirportResponse(Airport airport);
    List<AirportResponse> airportToAirportResponse(List<Airport> airports);
}


