package com.capair.api.util.maps;

import java.util.List;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.capair.api.model.Customer;
import com.capair.api.model.Itinerary;
import com.capair.api.model.response.CheckInResponse;
import com.capair.api.model.response.TripHolder;

@Mapper(componentModel = "spring")
public abstract class CheckInResponseMapper {

    @Autowired
    ItineraryMapper itineraryMapper;

    @Autowired 
    CustomerMapper customerMapper;

    public CheckInResponse tripHoldertoCheckInResponse(Itinerary itinerary, List<Customer> customers, TripHolder tripHolder){
        if (itinerary == null || customers == null || tripHolder == null){
            return null;
        }

        CheckInResponse response = new CheckInResponse();

        response.setItinerary(itineraryMapper.itineraryToItineraryResponse(itinerary));
        response.setCustomers(customerMapper.customerToCustomerResponse(customers));
        response.setInboundTrip(tripHolder.getInboundTrip());
        response.setOutboundTrip(tripHolder.getOutboundTrip());

        return response;
    }
}
