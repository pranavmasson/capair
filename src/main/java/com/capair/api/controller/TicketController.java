package com.capair.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capair.api.model.response.TicketResponse;
import com.capair.api.exception.NotFoundException;
import com.capair.api.service.TicketService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/ticket")
@CrossOrigin("*")
public class TicketController {
    
    @Autowired
    private TicketService ticketService;

    @Operation(summary="Returns all tickets", description="Tickets must exist")
    @ApiResponses(value={
        @ApiResponse(responseCode = "200", description="Tickets returned successfully"),
        @ApiResponse(responseCode = "404", description="Tickets not found")
    })
    @GetMapping
    public ResponseEntity<List<TicketResponse>> getTickets() {
        List<TicketResponse> response = ticketService.getTickets();
        if (response == null || response.size() == 0) {
            throw new NotFoundException("Tickets not found");
        }
        return ResponseEntity.ok(response);
    }

    @Operation(summary="Returns ticket with passed ID", description="Ticket must exist")
    @ApiResponses(value={
        @ApiResponse(responseCode = "200", description="Ticket returned successfully"),
        @ApiResponse(responseCode = "404", description="Ticket not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TicketResponse> getTicket(@PathVariable int id){
        //Error checking in TicketService
        return ResponseEntity.ok(ticketService.getTicket(id));
    }

    @Operation(summary="Checks in tickets with passed IDs", description="Tickets must exist")
    @ApiResponses(value={
        @ApiResponse(responseCode = "200", description="Tickets checked in successfully"),
        @ApiResponse(responseCode = "400", description="Tickets could not be checked in"),
        @ApiResponse(responseCode = "404", description="Tickets not found")
    })
    @CrossOrigin
    @PutMapping("/check-in")
    public ResponseEntity<String> ticketCheckedIn(@RequestBody List<Integer> ticketIds){
        if (ticketService.checkInTickets(ticketIds) == 0){
            return ResponseEntity.ok("Tickets successfully checked in");
        }
        return ResponseEntity
                    .badRequest()
                    .body("Tickets could not be checked in");
        
    }

}
