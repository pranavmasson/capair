package com.capair.api.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capair.api.model.request.BookingRequest;
import com.capair.api.model.request.FlightSearchRequest;
import com.capair.api.model.response.FlightResponse;
import com.capair.api.model.response.FlightSearchResponse;
import com.capair.api.model.response.Trip;
import com.capair.api.security.JwtGen;
import com.capair.api.exception.NotFoundException;
import com.capair.api.service.FlightService;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping(path="/flight")
@CrossOrigin("*")
public class FlightController {
    
    @Autowired
    private FlightService flightService;

    @Autowired
    private JwtGen jwtGen;

    @Operation(summary="Get all flights", description="Flights must exist")
    @ApiResponses(value={
        @ApiResponse(responseCode = "200", description="Flights returned successfully"),
        @ApiResponse(responseCode = "404", description="Flights not found")
    })
    @GetMapping
    public ResponseEntity<List<FlightResponse>> getFlights(){
        List<FlightResponse> response = flightService.getFlights();
        if (response == null || response.size() == 0){
            throw new NotFoundException("Flights not found");
        }
        return ResponseEntity.ok(response);
    }

    @Operation(summary="Get flight by ID", description="Flight ID must exist")
    @ApiResponses(value={
        @ApiResponse(responseCode = "200", description="Flight returned successfully"),
        @ApiResponse(responseCode = "404", description="Flight not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<FlightResponse> getFlight(@PathVariable String id){
        FlightResponse response = flightService.getFlightResponse(id);
        if (response == null){
            throw new NotFoundException("Flight with id:" + id + " not found");
        }
        return ResponseEntity.ok(response);
    }

    @Operation(summary="Search by source and destination airports, passenger count, and departure date", description="Fields must be valid and flights must match criteria")
    @ApiResponses(value={
        @ApiResponse(responseCode = "200", description="Flights returned successfully"),
        @ApiResponse(responseCode = "404", description="Flights not found")
    })
    @PostMapping("/search")
    public ResponseEntity<FlightSearchResponse> searchFlights(@RequestBody FlightSearchRequest searchRequest){

        FlightSearchResponse searchResponse; 
       
        if (searchRequest.isRoundTrip()){
            //outbound flight trips
            List<Trip> outboundFlights = flightService.searchFlights(searchRequest, LocalDateTime.now());

            if (outboundFlights == null || outboundFlights.size() == 0){
                throw new NotFoundException("Flights from:" + searchRequest.getSourceAirport() +" to:" + 
                    searchRequest.getDestinationAirport() + " on:" + searchRequest.getDepartureTime() + " not found");
            }

            //Find the latest outbound flight
            LocalDateTime latestFlight = LocalDateTime.now();
            for (Trip trip : outboundFlights){
                if (trip.getLatestFlight().isAfter(latestFlight)){
                    latestFlight = trip.getLatestFlight();
                }
            }

            //inbound flight trips
            List<Trip> inboundFlights = flightService.searchFlights(new FlightSearchRequest(searchRequest.getDestinationAirport(), 
                searchRequest.getSourceAirport(), searchRequest.getArrivalTime(), searchRequest.getDepartureTime(), searchRequest.getNumPassengers(), searchRequest.isRoundTrip()), latestFlight);

            if (inboundFlights == null || inboundFlights.size() == 0){
                throw new NotFoundException("Flights from:" + searchRequest.getDestinationAirport() + " to:" + 
                    searchRequest.getSourceAirport() + " on:" + searchRequest.getArrivalTime() + " not found");
            }

            searchResponse = new FlightSearchResponse(outboundFlights, inboundFlights);

        }
        else{
            
            //just outbound flight trips
            List<Trip> outboundFlights = flightService.searchFlights(searchRequest, LocalDateTime.now());

            if (outboundFlights == null || outboundFlights.size() == 0){
                throw new NotFoundException("Flights from:" + searchRequest.getSourceAirport() +" to:" + 
                    searchRequest.getDestinationAirport() + " on:" + searchRequest.getDepartureTime() + " not found");
            }

            searchResponse = new FlightSearchResponse(outboundFlights);
            
        }
        
        return ResponseEntity.ok(searchResponse);
    }

    @Operation(summary="Books flight for passed passengers", description="Fields must be valid and flights must match criteria")
    @ApiResponses(value={
        @ApiResponse(responseCode = "200", description="Flights booked successfully"),
        @ApiResponse(responseCode = "400", description="Itinerary could not be created")
    })
    @PostMapping("/book")
    public ResponseEntity<Integer> bookFlight(@RequestBody BookingRequest booking, @RequestHeader(value=HttpHeaders.AUTHORIZATION, required = false) String authHeader){
        //Exception checking in flight service
        String jwt = jwtGen.extractToken(authHeader);
        return ResponseEntity.ok(flightService.bookFlight(booking, jwt).getItineraryId());
    }
 
}
