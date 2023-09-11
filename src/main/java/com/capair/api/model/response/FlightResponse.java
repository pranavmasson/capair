package com.capair.api.model.response;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightResponse {

    private String flightID;

    @NotBlank
    private String sourceAirport;

    @NotBlank
    private String destinationAirport;

    @NotBlank
    private LocalDateTime departureTime;

    @NotBlank
    private LocalDateTime arrivalTime;
    
    @NotBlank
    private int econSeatsAvailable;

    @NotBlank
    private int busSeatsAvailable;

    @NotBlank
    private int economyPrice;

    @NotBlank
    private int businessPrice;
    
    @NotBlank
    private String airplaneType;
    
}
