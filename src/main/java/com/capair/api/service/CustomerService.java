package com.capair.api.service;

import java.util.List;

import com.capair.api.model.request.Passenger;
import com.capair.api.model.response.CustomerResponse;

public interface CustomerService {
    List<CustomerResponse> getCustomers();
    CustomerResponse getCustomer(int id);
    CustomerResponse searchCustomerByEmail(String email);
    void updateCustomer(int id, Passenger passenger);
}
