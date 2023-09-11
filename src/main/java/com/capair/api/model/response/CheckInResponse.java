package com.capair.api.model.response;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CheckInResponse {

    @NotBlank
    ItineraryResponse itinerary;

    @NotBlank
    List<CustomerResponse> customers;

    @NotBlank
    TripTicket outboundTrip;

    TripTicket inboundTrip;

    public CheckInResponse() {
        customers = new ArrayList<>();
    }
    
}
