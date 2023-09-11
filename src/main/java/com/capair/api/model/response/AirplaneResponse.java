package com.capair.api.model.response;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AirplaneResponse {
    
    @NotBlank
    private String id;

    @NotBlank
    private String manufacturer;

    @NotBlank
    private String model;

}
