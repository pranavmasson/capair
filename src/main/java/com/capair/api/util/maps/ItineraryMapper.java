package com.capair.api.util.maps;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import com.capair.api.dao.UserDao;
import com.capair.api.exception.NotFoundException;
import com.capair.api.model.Itinerary;
import com.capair.api.model.User;
import com.capair.api.model.request.BookingDetail;
import com.capair.api.model.request.BookingRequest;
import com.capair.api.model.response.ItineraryResponse;
import com.capair.api.security.JwtGen;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class ItineraryMapper {

    @Autowired
    JwtGen jwtGen;

    @Autowired
    UserDao userDao;
    
    public ItineraryResponse itineraryToItineraryResponse(Itinerary itinerary){
        if ( itinerary == null ) {
            return null;
        }

        ItineraryResponse itineraryResponse = new ItineraryResponse();

        if ( itinerary.getDestAirport() != null ) {
            itineraryResponse.setDestAirport( itinerary.getDestAirport() );
        }
        itineraryResponse.setFlightCount( itinerary.getFlightCount() );
        itineraryResponse.setItineraryId( itinerary.getItineraryId() );
        itineraryResponse.setRoundTrip( itinerary.isRoundTrip() );
        if ( itinerary.getSrcAirport() != null ) {
            itineraryResponse.setSrcAirport( itinerary.getSrcAirport() );
        }

        return itineraryResponse;
    }
    
    public List<ItineraryResponse> itineraryToItineraryResponse(List<Itinerary> itineraries){
        if ( itineraries == null ) {
            return null;
        }

        List<ItineraryResponse> list = new ArrayList<ItineraryResponse>( itineraries.size() );
        for ( Itinerary itinerary : itineraries ) {
            list.add( itineraryToItineraryResponse( itinerary ) );
        }

        return list;
    }

    public Itinerary bookingRequestToItinerary(BookingRequest request, String jwt, String sourceAirport, String destinationAirport){

        if (request == null){
            return null;
        }

        BookingDetail inboundDetails = request.getInboundTrip();
        BookingDetail outboundDetails = request.getOutboundTrip();

        Itinerary itinerary = new Itinerary();

        itinerary.setSrcAirport(sourceAirport);
        itinerary.setDestAirport(destinationAirport);

        if (inboundDetails != null){
            itinerary.setRoundTrip(true);
            itinerary.setFlightCount(outboundDetails.getFlightIds().size() + inboundDetails.getFlightIds().size());
        }
        else {
            itinerary.setRoundTrip(false);
            itinerary.setFlightCount(outboundDetails.getFlightIds().size());
        }

        if (jwt != null){
            User user = userDao.findByEmail(jwtGen.getUserNameFromJwtToken(jwt));
            if (user == null){
                throw new NotFoundException("User with email:" + jwtGen.getUserNameFromJwtToken(jwt) + " not found");
            }
            itinerary.setUserId(user.getId());
        }
        else {
            itinerary.setUserId(null);
        }

        return itinerary;
    }
}

