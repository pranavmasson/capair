package com.capair.api.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckInRequest {
    
    @NotBlank(message="Itinerary id is required")
    private int itineraryId;

    @NotBlank(message="Last name is required")
    private String lastName;
}
