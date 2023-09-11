package com.capair.api.service;

import java.util.List;

import com.capair.api.model.response.AirportResponse;

public interface AirportService {
    List<AirportResponse> getAirports();
}
