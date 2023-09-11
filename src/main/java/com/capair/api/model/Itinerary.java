package com.capair.api.model;

import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "itinerary") 
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Itinerary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "itinerary_id")
    private int itineraryId;

    @Column(name = "flight_count")
    @NotBlank
    private int flightCount;

    @Column(name = "start_airport")
    @NotBlank
    private String srcAirport;

    @Column(name = "end_airport")
    @NotBlank
    private String destAirport;

    @Column(name = "is_roundtrip")
    private boolean isRoundTrip;

    @Column(name = "user_id")
    private Integer userId;
    
}
