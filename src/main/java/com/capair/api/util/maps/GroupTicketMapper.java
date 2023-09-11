package com.capair.api.util.maps;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import com.capair.api.model.Ticket;
import com.capair.api.model.response.FlightResponse;
import com.capair.api.model.response.GroupTicketResponse;
import com.capair.api.model.response.TicketResponse;
import com.capair.api.util.DateUtils;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class GroupTicketMapper {
    
    @Autowired
    private FlightMapper flightMapper;

    public GroupTicketResponse ticketsToGroupTicketResponse(List<Ticket> tickets){

        if (tickets == null){
            return null;
        }

        GroupTicketResponse response = 
            flightMapper.flightToGroupTicketResponse(tickets.get(0).getFlight());
        response.setSeatSection(tickets.get(0).getSeatSection());
        for (Ticket ticket : tickets){
            response.addTicketId(ticket.getId());
        }

        return response;
    }

    public GroupTicketResponse flightResponseAndTicketResponseToGroupTicektResponse(FlightResponse flight, List<TicketResponse> tickets){
        
        if (tickets == null || flight == null){
            return null;
        }

        GroupTicketResponse response = new GroupTicketResponse();

        for (TicketResponse ticket : tickets){
            response.addTicketId(ticket.getId());
        }
    
        response.setFlightID(flight.getFlightID());
        response.setSourceAirport(flight.getSourceAirport());
        response.setDestinationAirport(flight.getDestinationAirport());
        response.setDepartureTime(flight.getDepartureTime());
        response.setArrivalTime(flight.getArrivalTime());
        response.setEconomyPrice(flight.getEconomyPrice());
        response.setBusinessPrice(flight.getBusinessPrice());
        response.setEconSeatsAvailable(flight.getEconSeatsAvailable());
        response.setAirplaneType(flight.getAirplaneType());
        response.setSeatSection(tickets.get(0).getSeatSection());
        response.setCheckInOpen(DateUtils.isCheckinOpen(flight.getDepartureTime()));
        response.setCheckedIn(tickets.get(0).isCheckedIn());


        return response;
    }

}
