package com.capair.api.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.capair.api.model.UserCustomer;

public interface UserCustomerDao extends JpaRepository<UserCustomer, Integer>{
    
    @Query(value="select customer_id from user_customer where user_id = :user_id",
    nativeQuery=true)
    List<Integer> findByUserId(
        @Param("user_id") int userId);

    boolean existsByUserIdAndCustomerCustomerId(int userId, int customerId);
    
}
