package com.capair.api.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capair.api.model.Airplane;

public interface AirplaneDao extends JpaRepository<Airplane, String> {
    
}
