package com.capair.api.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capair.api.dao.AirportDao;
import com.capair.api.model.response.AirportResponse;
import com.capair.api.service.AirportService;
import com.capair.api.util.maps.AirportMapper;

import jakarta.transaction.Transactional;



@Service
@Transactional
public class AirportServiceImpl implements AirportService{

    @Autowired
    private AirportDao airportDao;

    @Autowired
    private AirportMapper airportMapper;
    
    @Override
    public List<AirportResponse> getAirports() {
        return airportMapper.airportToAirportResponse(airportDao.findAll());
    }

}
