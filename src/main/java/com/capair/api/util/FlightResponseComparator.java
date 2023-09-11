package com.capair.api.util;

import java.util.Comparator;

import com.capair.api.model.response.FlightResponse;

public class FlightResponseComparator implements Comparator<FlightResponse>{
    @Override
    public int compare(FlightResponse flightResponseA,FlightResponse flightResponseB) {
        return flightResponseA.getDepartureTime().compareTo(flightResponseB.getDepartureTime());
    }
}
