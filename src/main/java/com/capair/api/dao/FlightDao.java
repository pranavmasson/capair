package com.capair.api.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.capair.api.model.Flight;

public interface FlightDao extends JpaRepository<Flight, String>{

    @Query(value = "SELECT * FROM flight f WHERE f.start_airport = :start_airport " + 
    "AND f.end_airport = :end_airport AND CAST(f.departure_time_utc AS DATE) = CAST(:dept_time AS DATE) " +
    "AND f.departure_time_utc > :now " +
    "AND :num_passengers <= (f.economy_seats + f.business_seats)", 
    nativeQuery = true)
    List<Flight> findDirectFlights(
        @Param("start_airport") String sourceAirport, 
        @Param("end_airport") String destinationAirport,
        @Param("num_passengers") int numPassengers,
        @Param("dept_time") String departureTime,
        @Param("now") LocalDateTime now
    );

        @Query(value = 
        "SELECT DISTINCT f1.flight_id AS flight1, " +
        "f2.flight_id AS flight2 " +
        "FROM flight f1 " +
        "LEFT JOIN flight f2 ON f1.end_airport = f2.start_airport " +
        "WHERE f2.departure_time_utc > f1.scheduled_arrival_time_utc " +
        "AND f1.start_airport = :start_airport " +
        "AND f2.end_airport = :end_airport " +
        "AND f1.economy_seats + f1.business_seats >= :num_passengers " +
        "AND f2.economy_seats + f2.business_seats >= :num_passengers " +
        "AND CAST(f1.departure_time_utc AS DATE) = CAST(:dept_time AS DATE) " +
        "AND f1.departure_time_utc > :now "+
        "AND TIMESTAMPDIFF(HOUR, f1.departure_time_utc, f2.departure_time_utc) <= 12 " +
        "AND TIMESTAMPDIFF(DAY, f1.departure_time_utc, f2.departure_time_utc) <= 1 " +
        "AND TIMESTAMPDIFF(MINUTE, f1.scheduled_arrival_time_utc, f2.departure_time_utc) >= 45 " +
        "AND (" +
        "   CAST(f1.scheduled_arrival_time_utc AS DATE) = CAST(f2.departure_time_utc AS DATE) " +
        "   OR (DATE(f2.departure_time_utc) = DATE_ADD(DATE(f1.departure_time_utc), INTERVAL 1 DAY) AND HOUR(f2.departure_time_utc) < HOUR(f1.departure_time_utc) + 12) " +
        ") " + 
        "LIMIT 15", 
        nativeQuery = true)
    List<List<String>> findFlightsWith1Layover(
        @Param("start_airport") String sourceAirport,
        @Param("end_airport") String destinationAirport,
        @Param("num_passengers") int numPassengers,
        @Param("dept_time") String departureTime,
        @Param("now") LocalDateTime now
    );

        @Query(value = 
        "SELECT DISTINCT f1.flight_id AS flight1, f2.flight_id AS flight2, f3.flight_id AS flight3 " +
        "FROM flight f1 " +
        "LEFT JOIN flight f2 ON f1.end_airport = f2.start_airport " +
        "LEFT JOIN flight f3 ON f2.end_airport = f3.start_airport " +
        "WHERE f2.departure_time_utc > f1.scheduled_arrival_time_utc " +
        "AND f3.departure_time_utc > f2.scheduled_arrival_time_utc " +
        "AND f1.start_airport = :start_airport " +
        "AND f3.end_airport = :end_airport " +
        "AND f1.economy_seats + f1.business_seats >= :num_passengers " +
        "AND f2.economy_seats + f2.business_seats >= :num_passengers " +
        "AND f3.economy_seats + f3.business_seats >= :num_passengers " +
        "AND CAST(f1.departure_time_utc AS DATE) = CAST(:dept_time AS DATE) " +
        "AND f1.departure_time_utc > :now "+
        "AND TIMESTAMPDIFF(HOUR, f1.departure_time_utc, f2.departure_time_utc) <= 12 " +
        "AND TIMESTAMPDIFF(HOUR, f2.departure_time_utc, f3.departure_time_utc) <= 12 " +
        "AND TIMESTAMPDIFF(MINUTE, f1.scheduled_arrival_time_utc, f2.departure_time_utc) >= 45 " +
        "AND TIMESTAMPDIFF(MINUTE, f2.scheduled_arrival_time_utc, f3.departure_time_utc) >= 45 " +
        "AND ( " +
        "   CAST(f1.scheduled_arrival_time_utc AS DATE) = CAST(f2.departure_time_utc AS DATE) " +
        "   OR (DATE(f2.departure_time_utc) = DATE_ADD(DATE(f1.departure_time_utc), INTERVAL 1 DAY) AND HOUR(f2.departure_time_utc) < HOUR(f1.departure_time_utc) + 12) " +
        ") " +
        "AND ( " +
        "   CAST(f2.scheduled_arrival_time_utc AS DATE) = CAST(f3.departure_time_utc AS DATE) " +
        "   OR (DATE(f3.departure_time_utc) = DATE_ADD(DATE(f2.departure_time_utc), INTERVAL 1 DAY) AND HOUR(f3.departure_time_utc) < HOUR(f2.departure_time_utc) + 12) " +
        ") " +
        "LIMIT 15", 
        nativeQuery = true)
    List<List<String>> findFlightsWith2Layovers(
        @Param("start_airport") String sourceAirport,
        @Param("end_airport") String destinationAirport,
        @Param("num_passengers") int numPassengers,
        @Param("dept_time") String departureTime,
        @Param("now") LocalDateTime now
    );

    //find flights by id
    Flight findByFlightId(String flightId);

 }

 