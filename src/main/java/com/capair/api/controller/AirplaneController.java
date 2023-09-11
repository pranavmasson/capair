package com.capair.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capair.api.model.response.AirplaneResponse;
import com.capair.api.exception.NotFoundException;
import com.capair.api.service.AirplaneService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/airplane")
@CrossOrigin("*")
public class AirplaneController {
    
    @Autowired
    private AirplaneService airplaneService;
  
    @Operation(summary="Gets all Airplanes", description="Airplanes must exist")
    @ApiResponses(value={
        @ApiResponse(responseCode = "200", description="Airplanes returned successfully"),
        @ApiResponse(responseCode = "404", description = "No Airplanes found")
    })
    @GetMapping
    public ResponseEntity<List<AirplaneResponse>> getAirplanes() {
        List<AirplaneResponse> response = airplaneService.getAirplanes();
        if (response == null || response.size() == 0) {
            throw new NotFoundException("Airplanes not found");
        }
        return ResponseEntity.ok(response);
    }

    @Operation(summary="Get Airplanes by ID", description="Airplane must exist")
    @ApiResponses(value={
        @ApiResponse(responseCode = "200", description="Airplane returned successfully"),
        @ApiResponse(responseCode = "404", description = "Invalid ID supplied")
    })
    @GetMapping("/search")
    public ResponseEntity<AirplaneResponse> getAirplaneById(@RequestBody String id) {
        AirplaneResponse response =airplaneService.getAirplaneById(id);
        if (response == null){
            throw new NotFoundException("Airplane with id:" + id + " not found");
        }
        return ResponseEntity.ok(response);
    }

}
