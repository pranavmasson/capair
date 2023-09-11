package com.capair.api.model;

import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "airport")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Airport {

    @Id
    @Column(name = "iata_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
   
    @Column(name = "city")
    @NotBlank
    private String city;

    @Column(name = "state")
    @NotBlank
    private String state;

    @Column(name = "airport_name")
    @NotBlank
    private String name;

    @Column(name = "gate_count")
    @NotBlank
    private int gate_count;

    @Column(name = "latitude")
    @NotBlank
    private int latitude;

    @Column(name = "longitude")
    @NotBlank
    private int longitude;

    @Column(name = "timezone")
    @NotBlank
    private String timezone;

    @Column(name="min_cap_op_per_hour")
    @NotBlank
    private int minCap;

    @Column(name="max_cap_op_per_hour")
    @NotBlank
    private int maxCap;

}
