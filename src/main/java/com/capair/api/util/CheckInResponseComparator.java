package com.capair.api.util;

import java.util.Comparator;

import com.capair.api.model.response.CheckInResponse;

public class CheckInResponseComparator implements Comparator<CheckInResponse>{
    @Override
    public int compare(CheckInResponse AllUserFlightsA, CheckInResponse AllUserFlightsB) {
        //Compares first flight of each trip to see which one is first
        return AllUserFlightsA.getOutboundTrip().getFlights().get(0).getDepartureTime()
            .compareTo(AllUserFlightsB.getOutboundTrip().getFlights().get(0).getDepartureTime());
    }
}
