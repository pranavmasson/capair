package com.capair.api.model.request;

import java.sql.Date;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRequest {

    private String firstName;

    private String lastName;

    @Email
    private String email;

    private String phoneNumber;

    private String address;

    private Date birthDate;

    private String gender;

    private String city;

    private String state;

    private String zipcode;

    private String password;
}
