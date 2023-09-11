package com.capair.api.dao;

import com.capair.api.model.Airport;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AirportDao extends JpaRepository<Airport, String> {
   
}
