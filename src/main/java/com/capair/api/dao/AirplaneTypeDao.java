package com.capair.api.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capair.api.model.AirplaneType;

public interface AirplaneTypeDao extends JpaRepository<AirplaneType, String> {
    
}
