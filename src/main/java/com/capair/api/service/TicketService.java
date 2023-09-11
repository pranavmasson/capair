package com.capair.api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.capair.api.model.response.TicketResponse;

@Service
public interface TicketService {
    List<TicketResponse> getTickets();
    TicketResponse getTicket(int id);
    List<TicketResponse> getTicketsWithFlight(int flightId, int itineraryId);
    int checkInTickets(List<Integer> ticketIds);
}
