package com.capair.api.model.request;

import java.util.List;

import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingRequest {
    
    @NotBlank(message = "No Passengers passed")
    private List<Passenger> passengers;

    @NotBlank(message = "No Outbound trips passed")
    private BookingDetail outboundTrip;

    @NotBlank
    private BookingDetail inboundTrip;

}
