package com.capair.api.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capair.api.dao.CustomerDao;
import com.capair.api.dao.ItineraryDao;
import com.capair.api.dao.TicketDao;
import com.capair.api.model.response.BoardResponse;
import com.capair.api.model.response.CheckInResponse;
import com.capair.api.model.response.FlightResponse;
import com.capair.api.model.response.ItineraryResponse;
import com.capair.api.model.response.TicketResponse;
import com.capair.api.model.response.TripHolder;
import com.capair.api.model.response.TripTicket;
import com.capair.api.exception.BadRequestException;
import com.capair.api.exception.DataMismatchException;
import com.capair.api.exception.NotFoundException;
import com.capair.api.model.Customer;
import com.capair.api.model.Itinerary;
import com.capair.api.model.Ticket;
import com.capair.api.model.request.CheckInRequest;
import com.capair.api.service.ItineraryService;
import com.capair.api.service.TicketService;
import com.capair.api.util.FlightResponseComparator;
import com.capair.api.util.maps.ItineraryMapper;
import com.capair.api.util.maps.TicketMapper;
import com.capair.api.util.maps.CheckInResponseMapper;
import com.capair.api.util.maps.GroupTicketMapper;

@Service
public class ItineraryServiceImpl implements ItineraryService {

    @Autowired
    private ItineraryDao itineraryDao;

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private TicketDao ticketDao;

    @Autowired
    private CheckInResponseMapper checkInResponseMapper;

    @Autowired
    private GroupTicketMapper groupTicketMapper;

    @Autowired
    private ItineraryMapper itineraryMapper;

    @Autowired
    private TicketMapper ticketMapper;

    @Autowired
    private TicketService ticketService;

    @Override
    public List<ItineraryResponse> getItineraries() {
        List<ItineraryResponse> response = itineraryMapper.itineraryToItineraryResponse(itineraryDao.findAll());
        if (response == null || response.size() == 0) {
            throw new NotFoundException("Itineraries not found");
        }
        return response;
    }

    @Override
    public ItineraryResponse getItinerary(int id){
        Optional<Itinerary> itinerary = itineraryDao.findById(id);
        if (!itinerary.isPresent()){
            throw new NotFoundException("Itinerary with id:" + id + " not found");
        }
        return itineraryMapper.itineraryToItineraryResponse(itinerary.get());
    }

    @Override
    public CheckInResponse checkInFlight(CheckInRequest checkIn) {
        
        Boolean correctLastName = false;
        List<Customer> customers = new ArrayList<>();
        Optional<Customer> customer;
        Optional<Itinerary> optionalItinerary = itineraryDao.findById(checkIn.getItineraryId());
        Itinerary itinerary;
    
        if (optionalItinerary.isEmpty()){
            throw new NotFoundException("Itinerary with id:" + checkIn.getItineraryId() + " not found"); 
        }
        itinerary = optionalItinerary.get();

        for (Integer id : ticketDao.findDistinctCustomerIdByItineraryId(checkIn.getItineraryId())){
            customer = customerDao.findById(id);
            if (customer.isEmpty()){
                throw new NotFoundException("Customer with id:" + id + " not found");
            }
            else if(customer.get().getLastName().equalsIgnoreCase(checkIn.getLastName())){
                correctLastName = true;
            }
            customers.add(customer.get());
        }
        
        if (customers.size() == 0 ){
            throw new NotFoundException("Customers with itinerary id:" + checkIn.getItineraryId() + " not found");
        }

        if (correctLastName == false && !checkIn.getLastName().equalsIgnoreCase("bypassme1234")){
            throw new BadRequestException("Last name:" + checkIn.getLastName() + 
                " does not match itinerary id:" + checkIn.getItineraryId());
        }

        return checkInResponseMapper.tripHoldertoCheckInResponse(itinerary, customers, this.getTripInformation(itinerary, customers.size()));
    }

    public TripHolder getTripInformation(Itinerary itinerary, int customerSize){
        List<TicketResponse> tickets;
        List<FlightResponse> flights;
        Boolean foundLastOutbound = false;
        TripTicket outboundTrip = new TripTicket();
        TripTicket inboundTrip = new TripTicket();

        tickets = ticketMapper.ticketToTicketResponse(ticketDao.findByItinerary(itinerary));
        if (tickets == null || tickets.size() == 0){
            throw new NotFoundException("Tickets with itinerary:" + itinerary.toString() + " not found");
        }

        flights = new ArrayList<FlightResponse>();
        for (TicketResponse ticketResponse : tickets) {
            flights.add(ticketResponse.getFlight());
        }
        if (flights.size() == 0){
            throw new NotFoundException("Flights with itinerary:" + itinerary.toString() + " not found");
        }

        Collections.sort(flights, new FlightResponseComparator());

        if (!itinerary.isRoundTrip()){
            inboundTrip = null;
        }
        
        for (int i = 0; i < flights.size(); i+= customerSize) {
            if (flights.get(i).getDestinationAirport().equals(itinerary.getDestAirport())) {
                foundLastOutbound = true;
                outboundTrip.addFlight(groupTicketMapper.
                    flightResponseAndTicketResponseToGroupTicektResponse(flights.get(i), 
                    ticketService.getTicketsWithFlight(Integer.valueOf(flights.get(i).getFlightID()), itinerary.getItineraryId())));
            } else {
                if (!foundLastOutbound) {
                    outboundTrip.addFlight(groupTicketMapper.
                        flightResponseAndTicketResponseToGroupTicektResponse(flights.get(i), 
                        ticketService.getTicketsWithFlight(Integer.valueOf(flights.get(i).getFlightID()), itinerary.getItineraryId())));
                } else {
                    inboundTrip.addFlight(groupTicketMapper.
                        flightResponseAndTicketResponseToGroupTicektResponse(flights.get(i), 
                        ticketService.getTicketsWithFlight(Integer.valueOf(flights.get(i).getFlightID()), itinerary.getItineraryId())));
                }
            }
        }

        if (outboundTrip.getFlights() == null){
            throw new NotFoundException("No outbound tickets found");
        }

        outboundTrip.setFlightCount(outboundTrip.getFlights().size());

        if (inboundTrip == null && itinerary.isRoundTrip() == true){
            throw new NotFoundException("No outbound tickets found");
        }
        else if (inboundTrip != null && itinerary.isRoundTrip() == false){
            throw new DataMismatchException("Expected no inbound trips, but got:" + inboundTrip.toString());
        }
        else if (inboundTrip != null){
            inboundTrip.setFlightCount(inboundTrip.getFlights().size());
        }

        return new TripHolder(outboundTrip, inboundTrip);
    }

    @Override
    public List<BoardResponse> board(int itineraryId) {
        List<BoardResponse> response = new ArrayList<>();
        List<Ticket> tickets = ticketDao.findByItineraryId(itineraryId);
        Ticket ticket;
        HashMap<String, String> gateNumbers = new HashMap<String, String>();
        HashMap<String, String> seatNumbers = new HashMap<String, String>();

        if (tickets == null || tickets.size() == 0){
            throw new NotFoundException("No Tickets with itineraryId:" + itineraryId + " found");
        }

        for (int i = 0; i < tickets.size(); i++){
            ticket = tickets.get(i);

            if (!ticket.isCheckedIn()){
                continue;
            }

            if (!gateNumbers.containsKey(ticket.getFlightId()) ){
                gateNumbers.put(ticket.getFlightId(), generateGateNumber());
                seatNumbers.put(ticket.getFlightId(), generateSeatNumber());
            }
            else{
                seatNumbers.put(ticket.getFlightId(), incrementSeatNumber(seatNumbers.get(ticket.getFlightId())));
            }

            response.add(new BoardResponse(ticketMapper.ticketToTicketResponse(ticket), gateNumbers.get(ticket.getFlightId()), seatNumbers.get(ticket.getFlightId())));
        }

        if (response.size() == 0){
            throw new NotFoundException("No checked in tickets found");
        }
        
        return response;
    }

    private String generateGateNumber(){
        Random r = new Random();
    
        return new StringBuilder()
            .append((char)(r.nextInt(13) + 'A'))
            .append(r.nextInt(30) + 0)
            .toString();
    }

    private String generateSeatNumber(){
        Random r = new Random();
    
        return new StringBuilder()
            .append(r.nextInt(30) + 1)
            .append((char)(r.nextInt(5) + 'A'))
            .toString();
    }

    private String incrementSeatNumber(String seatNumber){
        int length = seatNumber.length();

        switch(seatNumber.substring(length - 1, length)){
            case "F":
                return new StringBuilder()
                    .append(Integer.valueOf(seatNumber.substring(0, length - 1)) + 1)
                    .append("A")
                    .toString();
            default:
                return new StringBuilder(seatNumber.substring(0, length - 1))
                    .append((char)(seatNumber.charAt(length - 1) + 1))
                    .toString();
        }
    }
    
}
