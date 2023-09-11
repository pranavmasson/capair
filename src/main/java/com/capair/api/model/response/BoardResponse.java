package com.capair.api.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardResponse {

    private TicketResponse ticketResponse;

    private String gateNumber;

    private String seatNumber;
    
}
