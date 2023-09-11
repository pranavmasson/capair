package com.capair.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capair.api.exception.UnauthorizedException;
import com.capair.api.model.User;
import com.capair.api.model.request.RegisterRequest;
import com.capair.api.model.request.UserUpdateRequest;
import com.capair.api.model.request.LoginDetailsRequest;
import com.capair.api.model.request.Passenger;
import com.capair.api.model.request.PasswordRequest;
import com.capair.api.model.response.LoginResponse;
import com.capair.api.model.response.UserDetailsResponse;
import com.capair.api.model.response.UserFlightsResponse;
import com.capair.api.model.response.UserPassengerResponse;
import com.capair.api.security.JwtGen;
import com.capair.api.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping(value = "/user")
@CrossOrigin("*")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtGen jwtGen;

    @Operation(summary="Creates new user with passed email and password", description="Email must not already exist")
    @ApiResponses(value={
        @ApiResponse(responseCode = "200", description="User registered"),
        @ApiResponse(responseCode = "409", description="Email already exists")
    })
    @PostMapping("/register")
    public ResponseEntity<String> register(
            @RequestBody RegisterRequest registerRequest) {
        
        if(userService.existsAlready(registerRequest.getEmail())){
            return new ResponseEntity<>("Email exists already", HttpStatus.CONFLICT);
        }
       
        userService.addUser(registerRequest);

        return ResponseEntity.ok("User Registered");

    }

    @Operation(summary="Logs in user with passed email and password", description="Email and password must match")
    @ApiResponses(value={
        @ApiResponse(responseCode = "200", description="User logged in successfully"),
        @ApiResponse(responseCode = "401", description="Incorrect credentials")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginDetailsRequest loginDetailsRequest) {

        User existingUser = userService.getUser(loginDetailsRequest.getEmail());

        if(existingUser == null){
            throw new UnauthorizedException("Incorrect Credentials");
        }
        else if(!userService.passwordMatches(loginDetailsRequest.getPassword(), existingUser.getPassword())){
            throw new UnauthorizedException("Incorrect Credentials");
        }

        /*
        * Authenticate usr here using JWT authentication, provide a cookie with a day long expiration, and then 
        * use spring functions to allow for the user to remain logged in if they sign out or anything
        */
        LoginResponse response = userService.authenticate(loginDetailsRequest);

        if (loginDetailsRequest.getItineraryId() != null){
            userService.updateItinerary(loginDetailsRequest);
        }

        return ResponseEntity.ok().header("Set-Cookie", "jwt=" + response.getToken())
            .body(response);
        /**
         * return authentication token as well as the user id of the current user. 
        */

    }

    @Operation(summary="Logs out user", description="User must be logged in")
    @ApiResponses(value={
        @ApiResponse(responseCode = "200", description="User logged out successfully")
    })
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {

        return ResponseEntity.ok()
                    .header("Set-Cookie", "jwt=" + userService.logout())
                    .body("Successfully logged out");
        
    }

    @Operation(summary="Returns customer info for user", description="User must be logged in")
    @ApiResponses(value={
        @ApiResponse(responseCode = "200", description="User details returned successfully"),
        @ApiResponse(responseCode = "400", description="Invalid jwt token"),
        @ApiResponse(responseCode = "404", description="Customer not found")
    })
    @GetMapping("/details")
    public ResponseEntity<UserDetailsResponse> getUserDetails(@RequestHeader(value=HttpHeaders.AUTHORIZATION) String authHeader) {
        String jwt = jwtGen.extractToken(authHeader);
        return ResponseEntity.ok(userService.getUserDetails(jwt));
    }

    @Operation(summary="Returns flight info for user", description="User must be logged in")
    @ApiResponses(value={
        @ApiResponse(responseCode = "200", description="User flights returned successfully"),
        @ApiResponse(responseCode = "400", description="Invalid jwt token"),
        @ApiResponse(responseCode = "404", description="Flights not found")
    })
    @GetMapping("/flights")
    public ResponseEntity<UserFlightsResponse> getUserFlights(@RequestHeader(value=HttpHeaders.AUTHORIZATION) String authHeader){
        String jwt = jwtGen.extractToken(authHeader);
        return ResponseEntity.ok(userService.getUserFlights(jwt));
    }

    @Operation(summary="Updates customer info for user", description="User must be logged in")
    @ApiResponses(value={
        @ApiResponse(responseCode = "200", description="User details updated successfully"),
        @ApiResponse(responseCode = "400", description="Invalid jwt token"),
        @ApiResponse(responseCode = "404", description="User not found")
    })
    @PutMapping("/update")
    public ResponseEntity<String> updateUser(@RequestBody UserUpdateRequest userUpdateRequest, @RequestHeader(value=HttpHeaders.AUTHORIZATION) String authHeader){
        String jwt = jwtGen.extractToken(authHeader);
        return ResponseEntity.ok(userService.updateUser(userUpdateRequest, jwt));
    }

    @Operation(summary="Adds passenger info for user", description="User must be logged in")
    @ApiResponses(value={
        @ApiResponse(responseCode = "200", description="User passenger added successfully"),
        @ApiResponse(responseCode = "400", description="Invalid jwt token"),
        @ApiResponse(responseCode = "404", description="User not found")
    })
    @PostMapping("/passenger")
    public ResponseEntity<String> addPassenger(@RequestBody Passenger passenger, @RequestHeader(value=HttpHeaders.AUTHORIZATION) String authHeader){
        String jwt = jwtGen.extractToken(authHeader);
        if (jwt == null){
            throw new UnauthorizedException("Jwt token is not valid");
        }
        return ResponseEntity.ok(userService.addPassenger(passenger, jwt));
    }

    @Operation(summary="Returns passenger info for user", description="User must be logged in")
    @ApiResponses(value={
        @ApiResponse(responseCode = "200", description="User passengers returned successfully"),
        @ApiResponse(responseCode = "400", description="Invalid jwt token"),
        @ApiResponse(responseCode = "404", description="Passengers not found")
    })
    @GetMapping("/passenger")
    public ResponseEntity<UserPassengerResponse> getUserPassengers(@RequestHeader(value=HttpHeaders.AUTHORIZATION) String authHeader){
        String jwt = jwtGen.extractToken(authHeader);
        return ResponseEntity.ok(userService.getUserPassengers(jwt));
    }

    @Operation(summary="Updates password", description="User must be logged in")
    @ApiResponses(value={
        @ApiResponse(responseCode = "200", description="Password updated successfully"),
        @ApiResponse(responseCode = "400", description="Invalid jwt token")
    })
    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody PasswordRequest password, @RequestHeader(value=HttpHeaders.AUTHORIZATION) String authHeader){
        String jwt = jwtGen.extractToken(authHeader);
        if (jwt == null){
            throw new UnauthorizedException("Jwt token is not valid");
        }
        return ResponseEntity.ok(userService.changePassword(password, jwt));
    }
    
}
