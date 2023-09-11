package com.capair.api.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.capair.api.model.Itinerary;

public interface ItineraryDao extends JpaRepository<Itinerary, Integer> {

    @Query(value = "select itinerary_id from itinerary " +
    "where user_id = :user_id", 
    nativeQuery = true)
    List<Integer> findByItinerariesForUser(
        @Param("user_id") int userId);
    
}
