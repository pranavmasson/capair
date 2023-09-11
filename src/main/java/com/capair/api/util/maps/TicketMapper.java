package com.capair.api.util.maps;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

import com.capair.api.model.Ticket;
import com.capair.api.model.response.TicketResponse;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface TicketMapper {

    @Mapping(target="flight.arrivalTime", source="flight.scheduledArvTime")
    @Mapping(target="flight.flightID", source="flight.flightId")
    @Mapping(target="flight.departureTime", source="flight.departureTime")
    @Mapping(target="flight.destinationAirport", source="flight.destinationAirport")
    @Mapping(target="flight.sourceAirport", source="flight.sourceAirport")
    @Mapping(target="flight.econSeatsAvailable", source="flight.numEconomySeats")
    @Mapping(target="flight.busSeatsAvailable", source="flight.numBusinessSeats")
    TicketResponse ticketToTicketResponse(Ticket ticket);
    List<TicketResponse> ticketToTicketResponse(List<Ticket> tickets);
}

