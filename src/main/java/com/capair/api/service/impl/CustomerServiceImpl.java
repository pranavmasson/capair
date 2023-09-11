package com.capair.api.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.capair.api.dao.CustomerDao;
import com.capair.api.exception.NotFoundException;
import com.capair.api.model.Customer;
import com.capair.api.model.request.Passenger;
import com.capair.api.model.response.CustomerResponse;
import com.capair.api.service.CustomerService;
import com.capair.api.util.maps.CustomerMapper;

import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public List<CustomerResponse> getCustomers() {
        return customerMapper.customerToCustomerResponse(customerDao.findAll());
    }

    @Override
    public CustomerResponse getCustomer(int id) {
        Optional<Customer> customer = customerDao.findById(id);
        if (!customer.isPresent()) {
            throw new NotFoundException("Customer with id:" + id + " not found");
        }
        return customerMapper.customerToCustomerResponse(customer.get());
    }

    @Override
    public CustomerResponse searchCustomerByEmail(String email) {
        Customer customer = customerDao.findByEmail(email);
        if (customer == null) {
            throw new NotFoundException("Customer with email:" + email + " not found");
        }

        return customerMapper.customerToCustomerResponse(customer);
    }

    @Override
    public void updateCustomer(int id, Passenger passenger) {
        if (!customerDao.existsById(id)){
            throw new NotFoundException("The customer with id " + id + " does not exist");
        }
        System.out.println(passenger.toString());
        customerDao.save(customerMapper.updateCustomerFromRequest(customerDao.findById(id).get(), passenger));
    }
    
}
