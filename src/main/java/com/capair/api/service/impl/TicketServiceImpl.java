package com.capair.api.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capair.api.dao.TicketDao;
import com.capair.api.model.Ticket;
import com.capair.api.model.response.TicketResponse;
import com.capair.api.exception.BadRequestException;
import com.capair.api.exception.NotFoundException;
import com.capair.api.service.TicketService;
import com.capair.api.util.DateUtils;
import com.capair.api.util.maps.TicketMapper;

@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    private TicketDao ticketDao;

    @Autowired
    private TicketMapper ticketMapper;

    @Override
    public List<TicketResponse> getTickets() {
        List<TicketResponse> response = ticketMapper.ticketToTicketResponse(ticketDao.findAll());
        if (response == null || response.size() == 0) {
            throw new NotFoundException("Tickets not found");
        }
        return response;
    }

    @Override
    public List<TicketResponse> getTicketsWithFlight(int flightId, int itineraryId) {
        List<TicketResponse> response = ticketMapper.ticketToTicketResponse(ticketDao.findByFlightIdAndItineraryId(flightId, itineraryId));
        if (response == null || response.size() == 0) {
            throw new NotFoundException("Tickets with flight:" + flightId + " not found");
        }
        return response;
    }

    @Override
    public int checkInTickets(List<Integer> ticketIds) {
        List<Ticket> tickets = ticketDao.findAllById(ticketIds);
        if (tickets == null || tickets.size() == 0) {
            throw new NotFoundException("Tickets with ids:" + ticketIds.toString() + " not found");
        }

        for (Ticket ticket : tickets){
            if (ticket.isCheckedIn() == true){
                throw new BadRequestException("Ticket:" + ticket.toString() + " is already checked in");
            }
            else if (DateUtils.isCheckinOpen(ticket.getFlight().getDepartureTime()) == false){
                throw new BadRequestException("Ticket:" + ticket.toString() + " cannot be checked in, try again within 24 hours of flight");
            }
            ticket.setCheckedIn(true);
            ticketDao.save(ticket);
        }

        return 0;
    }

    @Override
    public TicketResponse getTicket(int id) {
        Optional<Ticket> ticket = ticketDao.findById(id);
        if (ticket.isEmpty()){
            throw new NotFoundException("Ticket with id:" + id + " not found");
        }
        return ticketMapper.ticketToTicketResponse(ticket.get());
    }
    
}
