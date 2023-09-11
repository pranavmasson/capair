package com.capair.api.model;

import java.sql.Date;

import com.capair.api.model.request.Passenger;
import com.capair.api.util.DateUtils;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "customer") 
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private int customerId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "telephone")
    private String phoneNumber;

    @Column(name = "street_address")
    private String address;

    @Column(name = "birthdate")
    private Date birthDate;

    @Column(name = "gender")
    private String gender;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "zipcode")
    private String zipcode;

    public Customer(Passenger passenger){
        this.firstName = passenger.getFirstName();
        this.lastName = passenger.getLastName();
        this.birthDate = DateUtils.createDateFromDateString(passenger.getBirthDate());
        this.email = passenger.getEmail();
        this.phoneNumber = passenger.getPhoneNumber();
    }

}
