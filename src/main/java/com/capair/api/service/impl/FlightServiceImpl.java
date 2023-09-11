package com.capair.api.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capair.api.dao.CustomerDao;
import com.capair.api.dao.FlightDao;
import com.capair.api.dao.ItineraryDao;
import com.capair.api.dao.TicketDao;
import com.capair.api.dao.UserCustomerDao;
import com.capair.api.dao.UserDao;
import com.capair.api.exception.BadRequestException;
import com.capair.api.exception.NotFoundException;
import com.capair.api.model.Customer;
import com.capair.api.model.Flight;
import com.capair.api.model.Itinerary;
import com.capair.api.model.Ticket;
import com.capair.api.model.User;
import com.capair.api.model.UserCustomer;
import com.capair.api.model.request.BookingDetail;
import com.capair.api.model.request.BookingRequest;
import com.capair.api.model.request.FlightSearchRequest;
import com.capair.api.model.request.Passenger;
import com.capair.api.model.response.FlightResponse;
import com.capair.api.model.response.ItineraryResponse;
import com.capair.api.model.response.Trip;
import com.capair.api.security.JwtGen;
import com.capair.api.service.FlightService;
import com.capair.api.util.maps.CustomerMapper;
import com.capair.api.util.maps.FlightMapper;
import com.capair.api.util.maps.ItineraryMapper;
import com.capair.api.util.maps.TripMapper;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class FlightServiceImpl implements FlightService {

    @Autowired
    private FlightDao flightDao;

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private TicketDao ticketDao;

    @Autowired
    private ItineraryDao itineraryDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserCustomerDao userCustomerDao;

    @Autowired 
    private CustomerMapper customerMapper;

    @Autowired 
    private FlightMapper flightMapper;

    @Autowired
    private ItineraryMapper itineraryMapper;

    @Autowired
    private TripMapper tripMapper;

    @Autowired
    private JwtGen jwtGen;

    @Override
    public List<FlightResponse> getFlights() {
        return flightMapper.flightToFlightResponse(flightDao.findAll());
    }

    @Override 
    public FlightResponse getFlightResponse(String id) {

        Optional<Flight> flight = flightDao.findById(id);

        if (!flight.isPresent()) {
			throw new NotFoundException("The flight with id " + id + " does not exist");
		}

        return flightMapper.flightToFlightResponse(flight.get());
    }

    @Override 
    public Flight getFlight(String id) {

        Optional<Flight> flight = flightDao.findById(id);

        if (!flight.isPresent()) {
			throw new NotFoundException("The flight with id " + id + " does not exist");
		}

        return flight.get();
    }

    @Override
    public List<Trip> searchFlights(FlightSearchRequest searchRequest, LocalDateTime startTime) {

        List<Flight> directFlights = flightDao.findDirectFlights(
            searchRequest.getSourceAirport(),
            searchRequest.getDestinationAirport(),
            searchRequest.getNumPassengers(),
            searchRequest.getDepartureTime(),
            startTime
        );

        List<Trip> trips = new ArrayList<>();

        //DIRECT FLIGHTS -> one trip per flight
        for(Flight f1 : directFlights){
            List<FlightResponse> advancedflightList = new ArrayList<>();
            advancedflightList.add(flightMapper.flightToFlightResponse(f1));

            trips.add(tripMapper.flightResponsetoTrip(advancedflightList, f1.getEconomyPrice(),
                f1.getBusinessPrice(), f1.getScheduledArvTime()));
        }

        //layover flight handling
        List<List<String>> flightsWith1Lay = flightDao.findFlightsWith1Layover(
            searchRequest.getSourceAirport(),
            searchRequest.getDestinationAirport(),
            searchRequest.getNumPassengers(),
            searchRequest.getDepartureTime(),
            startTime
        );

        addTrips(trips, flightsWith1Lay);

         //LAYOVER FLIGHTS -> one trip per path of flights
        List<List<String>> flightsWith2Lays = flightDao.findFlightsWith2Layovers(
            searchRequest.getSourceAirport(),
            searchRequest.getDestinationAirport(),
            searchRequest.getNumPassengers(),
            searchRequest.getDepartureTime(),
            startTime
        );

        addTrips(trips, flightsWith2Lays);

        //Create and return the flight search response containing all the trips
        return trips;   
    }

    private void addTrips(List<Trip> trips, List<List<String>> layoverList){

        int totalEconomyCost = 0;
        int totalBusinessCost = 0;
        LocalDateTime latestFlight = LocalDateTime.now();

        for(List<String> layovers : layoverList) {

            List<FlightResponse> advancedflightList = new ArrayList<>();

            totalEconomyCost = 0;
            totalBusinessCost = 0;
            int flightCount  = 0;
            latestFlight = LocalDateTime.now();

            for(String layover : layovers){

                Flight f1 = flightDao.findByFlightId(layover);

                flightCount++;

                if(flightCount > 1){
                    totalEconomyCost += (f1.getEconomyPrice() * .5);
                    totalBusinessCost += (f1.getBusinessPrice() * .4);
                
                }
                else{
                    totalEconomyCost += f1.getEconomyPrice();
                    totalBusinessCost += f1.getBusinessPrice();

                }
                if (f1.getScheduledArvTime().isAfter(latestFlight)){
                    latestFlight = f1.getScheduledArvTime();
                }

                advancedflightList.add(flightMapper.flightToFlightResponse(f1));
            }

            trips.add(tripMapper.flightResponsetoTrip(advancedflightList, totalEconomyCost, totalBusinessCost, latestFlight));
        }
    }

    @Override
    public ItineraryResponse bookFlight(BookingRequest bookRequest, String jwt) {

        Itinerary itinerary;
        Customer customer;
        User user = null; 
        BookingDetail inboundDetails = bookRequest.getInboundTrip();
        BookingDetail outboundDetails = bookRequest.getOutboundTrip();

        itinerary = itineraryMapper.bookingRequestToItinerary(
            bookRequest,
            jwt,
            this.getFlight(outboundDetails.getFlightIds().get(0)).getSourceAirport(), 
            this.getFlight(outboundDetails.getFlightIds().get(outboundDetails.getFlightIds().size() - 1)).getDestinationAirport()
        );

        if (itinerary == null) {
            throw new BadRequestException("Itinerary could not be created from:" + bookRequest.toString());
        }

        if (jwt != null) {
            user = userDao.findByEmail(jwtGen.getUserNameFromJwtToken(jwt));
        } 

        itineraryDao.save(itinerary);

        for (Passenger passenger : bookRequest.getPassengers()) {

            customer = customerDao.findByEmail(passenger.getEmail());

            if (customer == null) {
                customer = customerMapper.passengerToCustomer(passenger);
                customerDao.save(customer);
            }

            if (user != null && user.getCustomer() == null && passenger.isAddCustomer()){
                user.setCustomer(customer);
                userCustomerDao.save(new UserCustomer(user, customer));
            }

            if (passenger.isAddCustomer() && user != null &&  customer.getCustomerId() != user.getCustomer().getCustomerId() && !userCustomerDao.existsByUserIdAndCustomerCustomerId(user.getId(), customer.getCustomerId())){
                userCustomerDao.save(new UserCustomer(user, customer));
            }

            bookTrip(outboundDetails, itinerary, customer);

            if (bookRequest.getInboundTrip() != null) {
                bookTrip(inboundDetails, itinerary, customer);
            }
        }
        
        return itineraryMapper.itineraryToItineraryResponse(itinerary);
    }

    private void bookTrip(BookingDetail bookDetail, Itinerary itinerary, Customer customer){
        Flight flight;
        for (String flightId : bookDetail.getFlightIds()){
            flight = getFlight(flightId);
                    if (flight.getSeatsOfSection(bookDetail.getSeatSection()) < 1){
                        throw new BadRequestException("The " + bookDetail.getSeatSection() + " section of this flight is full");
                    }
                    ticketDao.save(new Ticket(itinerary, customer, flight , bookDetail.getSeatSection()));
                    flight.decrementSectionSeats(bookDetail.getSeatSection());
        }
    }
    
}
