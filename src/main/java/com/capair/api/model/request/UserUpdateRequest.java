package com.capair.api.model.request;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {

    @NotBlank
    @JsonProperty
    private String firstName;

    @NotBlank
    @JsonProperty
    private String lastName;

    @NotBlank
    @JsonProperty
    private String phoneNumber;

    @NotBlank
    @JsonProperty
    private Date birthDate;

    @NotBlank
    @Email
    @JsonProperty
    private String email;

    @NotBlank
    @JsonProperty
    private String password;

}
