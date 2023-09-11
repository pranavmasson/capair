package com.capair.api.model.request;

import java.util.List;

import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDetail {
    
    @NotBlank
    private List<String> flightIds;

    @NotBlank
    private String seatSection;

}
