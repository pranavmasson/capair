package com.capair.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItineraryResponse {

    @NotBlank
    private int itineraryId;

    @NotBlank
    private int flightCount;

    @NotBlank
    private String srcAirport;

    @NotBlank
    private String destAirport;

    @JsonProperty
    private boolean isRoundTrip;
    
    private Integer userId;

}
