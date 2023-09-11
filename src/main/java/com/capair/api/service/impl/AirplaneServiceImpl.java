package com.capair.api.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capair.api.dao.AirplaneDao;
import com.capair.api.exception.NotFoundException;
import com.capair.api.model.Airplane;
import com.capair.api.model.response.AirplaneResponse;
import com.capair.api.service.AirplaneService;
import com.capair.api.util.maps.AirplaneMapper;

@Service
public class AirplaneServiceImpl implements AirplaneService {

    @Autowired
    private AirplaneDao airplaneDao;

    @Autowired
    private AirplaneMapper airplaneMapper;

    @Override
    public List<AirplaneResponse> getAirplanes() {
        List<Airplane> response = airplaneDao.findAll();
        if (response == null || response.size() == 0){
            throw new NotFoundException("Airplanes not found");
        }
        return airplaneMapper.airplaneToAirplaneResponse(response);
    }

    @Override
    public AirplaneResponse getAirplaneById(String id) {
        Optional<Airplane> optionalAirplane = airplaneDao.findById(id);
        if (optionalAirplane.isEmpty()){
            throw new NotFoundException("Airplane with id:" + id + " not found");
        }
        return airplaneMapper.airplaneToAirplaneResponse(optionalAirplane.get());
    }
    
}
