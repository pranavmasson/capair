package com.capair.api.model.response;


import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * A TRIP is essentially an objet used to hold lists of connected flights
 * WITH extra data that is sourced from other tables in the database
 * This object is returned to the frontend for data display and booking flow
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Trip {
    
    @NotBlank
    private int flightCount;
   
    @NotBlank
    private int totalEconomyCost;

    @NotBlank
    private int totalBusinessCost;

    private List<FlightResponse> flights;

    private LocalDateTime latestFlight;

    public Trip(List<FlightResponse> flightList){
        this.flightCount = 0; 
        this.flights = flightList;
        if(flightList != null){
            this.flightCount = flightList.size();
        }
    
    }

}
