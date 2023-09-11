package com.capair.api.model;
import java.time.LocalDateTime;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;
import com.capair.api.util.FlightConstants;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "flight")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Flight {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "flight_id")
    private String flightId;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "airplane_id")
    @NotBlank
    private Airplane airplane;

    @Column(name = "start_airport")
    @NotBlank
    private String sourceAirport;

    @Column(name = "end_airport")
    @NotBlank
    private String destinationAirport;

    @Column(name = "departure_time_utc")
    @NotBlank
    private LocalDateTime departureTime;

    @Column(name = "scheduled_arrival_time_utc")
    @NotBlank
    private LocalDateTime scheduledArvTime;

    @Column(name = "economy_seats")
    @NotBlank
    private int numEconomySeats;

    @Column(name = "business_seats")
    @NotBlank
    private int numBusinessSeats;

    @Column(name = "first_class_seats")
    @NotBlank
    private int numFirstClassSeats;

    @Column(name = "premium_seats")
    @NotBlank
    private int numPremiumSeats;

    @Column(name = "economy_price")
    @NotBlank
    private int economyPrice;

    @Column(name = "business_price")
    @NotBlank
    private int businessPrice;

    @Column(name = "first_class_price")
    @NotBlank
    private int firstClassPrice;

    @Column(name = "premium_price")
    @NotBlank
    private int premiumPrice;

    public String getAirplaneType(){
        return  this.getAirplane().getAirplaneType().getManufacturer() + " " + this.getAirplane().getAirplaneType().getModel();
    }

    public int getSeatsOfSection(String section){
        if (section.equals(FlightConstants.ECONOMY.getValue())){
            return numEconomySeats;
        }
        else if (section.equals(FlightConstants.PREMIUM.getValue())){
            return numPremiumSeats;
        }
        else if (section.equals(FlightConstants.BUSINESS.getValue())){
            return numBusinessSeats;
        }
        else if (section.equals(FlightConstants.FIRST_CLASS.getValue())){
            return numFirstClassSeats;
        }
        throw new IllegalArgumentException("Invalid section: " + section);
    }

    public void decrementSectionSeats(String section){
        if (section.equals(FlightConstants.ECONOMY.getValue())){
            numEconomySeats-=1;
        }
        else if (section.equals(FlightConstants.PREMIUM.getValue())){
            numPremiumSeats-=1;
        }
        else if (section.equals(FlightConstants.BUSINESS.getValue())){
            numBusinessSeats-=1;
        }
        else if (section.equals(FlightConstants.FIRST_CLASS.getValue())){
            numFirstClassSeats-=1;
        }
    }

}
