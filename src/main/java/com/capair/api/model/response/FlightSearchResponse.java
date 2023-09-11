package com.capair.api.model.response;

import java.util.List;

import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightSearchResponse {
    
    @NotBlank
    private List<Trip> outboundTrips;

    @NotBlank
    private List<Trip> inboundTrips;

    public FlightSearchResponse(List<Trip> outboundFlights) {
        this.outboundTrips = outboundFlights;
        this.inboundTrips = null;
    }

}
