package com.capair.api.model.request;

import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightSearchRequest {
    
    @NotBlank
    private String sourceAirport;

    @NotBlank
    private String destinationAirport;

    @NotBlank
    private String departureTime;

    @NotBlank
    private String arrivalTime;

    @NotBlank
    private int numPassengers;

    @JsonProperty
    @NotBlank
    private boolean isRoundTrip;

}
