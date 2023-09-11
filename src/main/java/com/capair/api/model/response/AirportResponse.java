package com.capair.api.model.response;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AirportResponse {
    
    @NotBlank
    private String id;
   
    @NotBlank
    private String city;

    @NotBlank
    private String state;

    @NotBlank
    private String name;

}
