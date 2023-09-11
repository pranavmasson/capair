package com.capair.api.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.capair.api.model.Itinerary;
import com.capair.api.model.Ticket;
import java.util.List;

public interface TicketDao extends JpaRepository<Ticket, Integer> {
    List<Ticket> findByItinerary(Itinerary itinerary);

    @Query(value = "select distinct(customer_id) from ticket " +
    "where itinerary_id = :itinerary_id", 
    nativeQuery = true)
    List<Integer> findDistinctCustomerIdByItineraryId(
        @Param("itinerary_id") int id);

    @Query(value = "select * from ticket t where t.flight_id = :flight_id " +
    "and t.itinerary_id = :itinerary_id",
    nativeQuery = true)
    List<Ticket> findByFlightIdAndItineraryId(
        @Param("flight_id") int flightId, 
        @Param("itinerary_id") int itineraryId);

    @Query(value = "select * from ticket t where t.itinerary_id = :itinerary_Id",
    nativeQuery = true)
    List<Ticket> findByItineraryId(
        @Param("itinerary_Id") int itineraryId);

}
