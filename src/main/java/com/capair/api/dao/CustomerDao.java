package com.capair.api.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.capair.api.model.Customer;

public interface CustomerDao extends JpaRepository<Customer, Integer> {
    Customer findByEmail(String email);
    Customer findByLastName(String lastName);
    
}
