package com.capair.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capair.api.model.request.CheckInRequest;
import com.capair.api.model.response.BoardResponse;
import com.capair.api.model.response.CheckInResponse;
import com.capair.api.model.response.ItineraryResponse;
import com.capair.api.exception.NotFoundException;
import com.capair.api.service.ItineraryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/itinerary")
@CrossOrigin("*")
public class ItineraryController {
    
    @Autowired
    private ItineraryService itineraryService;

    @Operation(summary="Returns itinerary of passed ID", description="Itinerary with ID must exist")
    @ApiResponses(value={
        @ApiResponse(responseCode = "200", description="Itinerary returned successfully"),
        @ApiResponse(responseCode = "404", description="Itinerary not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ItineraryResponse> getItinerary(@PathVariable int id) {
        ItineraryResponse response = itineraryService.getItinerary(id);
        if (response == null){
            throw new NotFoundException("Itinerary with id:" + id + " not found");
        }
        return ResponseEntity.ok(response);
    }

    @Operation(summary="Returns itinerary matching Id and last name passed", description="Itinerary with ID must exist and passengers with last name must have last name")
    @ApiResponses(value={
        @ApiResponse(responseCode = "200", description="Itinerary returned successfully"),
        @ApiResponse(responseCode = "400", description="Last names do not match"),
        @ApiResponse(responseCode = "404", description="Itinerary not found"),
        @ApiResponse(responseCode = "406", description="Itinerary not valid")
    })
    @PostMapping("/check-in")
    public ResponseEntity<CheckInResponse> checkInFlight(@RequestBody CheckInRequest checkIn){
        //Exception checking in flight service
        return ResponseEntity.ok(itineraryService.checkInFlight(checkIn));
    }

    @Operation(summary="Returns boarding pass of passed itinerary ID", description="Itinerary with ID must exist")
    @ApiResponses(value={
        @ApiResponse(responseCode = "200", description="Boarding pass returned successfully"),
        @ApiResponse(responseCode = "404", description="Itinerary not found")
    })
    @PostMapping("/board")
    public ResponseEntity<List<BoardResponse>> board(@RequestBody int board){
        return ResponseEntity.ok(itineraryService.board(board));
    }

}
