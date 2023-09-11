package com.capair.api.model.response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GroupTicketResponse {
    
    @NotBlank
    private List<Integer> ticketIds;

    @NotBlank
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
    private int economyPrice;

    @NotBlank
    private int businessPrice;
    
    @NotBlank
    private int econSeatsAvailable;

    @NotBlank
    private int busSeatsAvailable;
    
    @NotBlank
    private String airplaneType;

    @NotBlank
    private String seatSection;

    @NotBlank
    private boolean checkInOpen;

    @NotBlank
    private boolean checkedIn;

    public GroupTicketResponse(){
        ticketIds = new ArrayList<>();
    }

    public void addTicketId(int ticketId){
        ticketIds.add(ticketId);
    }

}
