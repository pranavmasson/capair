package com.capair.api.model;

import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ticket")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    private int id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "itinerary_id", nullable = false)
    @NotBlank
    private Itinerary itinerary;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", nullable = false)
    @NotBlank
    private Customer customer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="flight_id", nullable = false)
    @NotBlank
    private Flight flight;

    @Column(name="seat_section")
    @NotBlank
    private String seatSection;

    @Column(name="checkedIn")
    @NotBlank
    private boolean checkedIn;

    public Ticket(Itinerary itinerary, Customer customer, Flight flight, String seatSection){
        this.itinerary = itinerary;
        this.customer = customer;
        this.flight = flight;
        this.seatSection = seatSection;
    }

    public String getFlightId(){
        return this.flight.getFlightId();
    }
    
}
