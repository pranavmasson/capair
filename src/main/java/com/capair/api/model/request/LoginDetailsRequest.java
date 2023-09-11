package com.capair.api.model.request;

import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDetailsRequest {

    @NotBlank
    @Email
    @JsonProperty
    private String email;

    @NotBlank
    @JsonProperty
    private String password;

    @JsonProperty
    private Integer itineraryId;
    
}
