package com.capair.api.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponse {
    
    private int customerId;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

}
