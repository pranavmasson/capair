package com.capair.api.model.response;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketResponse {
    
    @NotBlank
    private int id;

    private ItineraryResponse itinerary;

    @NotBlank
    private CustomerResponse customer;

    @NotBlank
    private FlightResponse flight;

    @NotBlank
    private String seatSection;

    @NotBlank
    private boolean checkedIn;

}
