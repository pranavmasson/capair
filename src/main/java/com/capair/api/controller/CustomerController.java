package com.capair.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capair.api.model.request.Passenger;
import com.capair.api.model.response.CustomerResponse;
import com.capair.api.exception.NotFoundException;
import com.capair.api.service.CustomerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/customer")
@CrossOrigin("*")
public class CustomerController {
    
    @Autowired
    private CustomerService customerService;

    @Operation(summary="Get all customers", description="Customers must exist")
    @ApiResponses(value={
        @ApiResponse(responseCode = "200", description="Customers found"),
        @ApiResponse(responseCode = "404", description="Customers not found")
    })
    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getCustomers() {
        List<CustomerResponse> response = customerService.getCustomers();
        if (response == null || response.size() == 0) {
            throw new NotFoundException("Customers not found");
        }
        return ResponseEntity.ok(response);
    }

    @Operation(summary="Get customer by ID", description="Customer must exist")
    @ApiResponses(value={
        @ApiResponse(responseCode = "200", description="Customer found"),
        @ApiResponse(responseCode = "404", description="Invalid customer ID supplied")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomer(@PathVariable("id") int id) {
        CustomerResponse response = customerService.getCustomer(id);
        if (response == null){
            throw new NotFoundException("Customer with id:" + id + " not found");
        }
        return ResponseEntity.ok(response);
    }

    @Operation(summary="Get customer by email", description="Customer must exist")
    @ApiResponses(value={
        @ApiResponse(responseCode = "200", description="Customer found"),
        @ApiResponse(responseCode = "404", description="Invalid customer email supplied")
    })
    @GetMapping("/search/{email}")
    public ResponseEntity<CustomerResponse> searchCustomerByEmail(@PathVariable("email") String email) {
        CustomerResponse response =  customerService.searchCustomerByEmail(email);
        if (response == null){
            throw new NotFoundException("Customer with email:" + email + " not found");
        }
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary="Update customer by ID", description="Customer must exist")
    @ApiResponses(value={
        @ApiResponse(responseCode = "200", description="Customer updated successfully"),
        @ApiResponse(responseCode = "404", description="Invalid customer ID supplied")
    })
    @PutMapping("/{id}")
    public ResponseEntity<String> updateCustomer(@PathVariable("id") int id, @RequestBody Passenger passenger) {
        //Exceptions thrown in customer service
        customerService.updateCustomer(id, passenger);
        return ResponseEntity.ok("Customer updated successfully");
    }
    
}
