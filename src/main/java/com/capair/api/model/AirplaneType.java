package com.capair.api.model;

import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

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
@Table(name = "airplane_type")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AirplaneType {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "airplane_type")
    private String airplaneType;

    @Column(name= "manufacturer")
    @NotBlank
    private String manufacturer;

    @Column(name= "model")
    @NotBlank
    private String model;

    @Column(name = "premium_cap")
    @NotBlank
    private int premCap;

    @Column(name = "first_class_cap")
    @NotBlank
    private int fcCap;

    @Column(name = "business_class_cap")
    @NotBlank
    private int bcCap;

    @Column(name = "economy_cap")
    @NotBlank
    private int eCap;

    @Column(name = "bulkhead_cap")
    @NotBlank
    private int bulkheadCap;

    @Column(name = "exit_cap")
    @NotBlank
    private int exitCap;

    @Column(name="crew_cap")
    @NotBlank
    private int crewCap;

    @Column(name = "pilot_needed")
    @NotBlank
    private int pilotNeeded;

    @Column(name = "fuel_cap_liters")
    @NotBlank
    private float fuelCap;

    @Column(name = "weight_cap_kg")
    @NotBlank
    private float weightCap;

    @Column(name = "range_dist")
    @NotBlank
    private int range;

    @Column(name = "cruise")
    @NotBlank
    private int cruise;

    @Column(name = "lavatories")
    @NotBlank
    private int lavatories;

    @Column(name = "exits")
    @NotBlank
    private int exits;

}
