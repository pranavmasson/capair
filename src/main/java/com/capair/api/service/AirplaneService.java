package com.capair.api.service;

import java.util.List;

import com.capair.api.model.response.AirplaneResponse;

public interface AirplaneService {
    List<AirplaneResponse> getAirplanes();
    AirplaneResponse getAirplaneById(String id);
}
