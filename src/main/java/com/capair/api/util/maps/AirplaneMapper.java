package com.capair.api.util.maps;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.capair.api.model.Airplane;
import com.capair.api.model.response.AirplaneResponse;

@Mapper(componentModel = "spring")
public interface AirplaneMapper {
    
    @Mapping(target="model", source="airplaneType.model")
    @Mapping(target="manufacturer", source="airplaneType.manufacturer")
    AirplaneResponse airplaneToAirplaneResponse(Airplane airplane);
    List<AirplaneResponse> airplaneToAirplaneResponse(List<Airplane> airplanes);
}

