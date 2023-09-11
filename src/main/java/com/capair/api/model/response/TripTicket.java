package com.capair.api.model.response;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TripTicket {
    
    @NotBlank
    int flightCount;
    
    @NotBlank
    List<GroupTicketResponse> flights;

    public TripTicket(){
        flights = new ArrayList<>();
    }

    public void addFlight(GroupTicketResponse flight) {
        flights.add(flight);
    }

}
