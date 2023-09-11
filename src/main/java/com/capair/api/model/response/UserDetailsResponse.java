package com.capair.api.model.response;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsResponse {

    private Integer customerId;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private Date birthDate;

    private String email;

    private boolean customerCreated;

}
