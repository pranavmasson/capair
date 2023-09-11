package com.capair.api.service;

import org.springframework.stereotype.Service;
import com.capair.api.model.User;
import com.capair.api.model.request.LoginDetailsRequest;
import com.capair.api.model.request.Passenger;
import com.capair.api.model.request.PasswordRequest;
import com.capair.api.model.request.RegisterRequest;
import com.capair.api.model.request.UserUpdateRequest;
import com.capair.api.model.response.LoginResponse;
import com.capair.api.model.response.UserDetailsResponse;
import com.capair.api.model.response.UserFlightsResponse;
import com.capair.api.model.response.UserPassengerResponse;

@Service
public interface UserService {

    boolean existsAlready(String email);
    void addUser(RegisterRequest registerRequest);
    boolean passwordMatches(String email, String passwordFromUser);
    User getUser(String email);
    String logout();
    LoginResponse authenticate(LoginDetailsRequest loginDetails);
    void updateItinerary(LoginDetailsRequest loginDetails);
    UserDetailsResponse getUserDetails(String jtw);
    UserFlightsResponse getUserFlights(String jwt);
    String updateUser(UserUpdateRequest req, String jwt);
    String addPassenger(Passenger passenger, String jwt);
    UserPassengerResponse getUserPassengers(String jwt);
    String changePassword(PasswordRequest passwords, String jwt);
}
