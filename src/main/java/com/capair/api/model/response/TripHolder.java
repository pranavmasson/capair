package com.capair.api.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TripHolder {

    TripTicket outboundTrip;

    TripTicket inboundTrip;
    
}
