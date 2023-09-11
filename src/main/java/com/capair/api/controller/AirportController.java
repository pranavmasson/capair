package com.capair.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capair.api.model.response.AirportResponse;
import com.capair.api.exception.NotFoundException;
import com.capair.api.service.AirportService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/airport")
@CrossOrigin("*")
public class AirportController {

    @Autowired
    private AirportService airportService;

    @Operation(summary="Returns all Airports", description="Airports must exist")
    @ApiResponses(value={
        @ApiResponse(responseCode = "200", description="Airports returned successfully"),
        @ApiResponse(responseCode = "404", description = "No Airports found")
    })
    @GetMapping
    public ResponseEntity<List<AirportResponse>> getAirports() {
        List<AirportResponse> response = airportService.getAirports();
        if (response == null || response.size() == 0) {
            throw new NotFoundException("Airports not found");
        }
        return ResponseEntity.ok(response);
    }

}
