package com.capair.api.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.capair.api.dao.CustomerDao;
import com.capair.api.dao.ItineraryDao;
import com.capair.api.dao.UserCustomerDao;
import com.capair.api.dao.UserDao;
import com.capair.api.exception.BadRequestException;
import com.capair.api.exception.NotFoundException;
import com.capair.api.exception.UnauthorizedException;
import com.capair.api.model.Customer;
import com.capair.api.model.Itinerary;
import com.capair.api.model.User;
import com.capair.api.model.UserCustomer;
import com.capair.api.model.request.CheckInRequest;
import com.capair.api.model.request.LoginDetailsRequest;
import com.capair.api.model.request.Passenger;
import com.capair.api.model.request.PasswordRequest;
import com.capair.api.model.request.RegisterRequest;
import com.capair.api.model.request.UserUpdateRequest;
import com.capair.api.model.response.CheckInResponse;
import com.capair.api.model.response.UserDetailsResponse;
import com.capair.api.model.response.UserFlightsResponse;
import com.capair.api.model.response.UserPassenger;
import com.capair.api.model.response.UserPassengerResponse;
import com.capair.api.model.response.LoginResponse;
import com.capair.api.security.JwtGen;
import com.capair.api.security.services.UserDetailsImpl;
import com.capair.api.service.ItineraryService;
import com.capair.api.service.UserService;
import com.capair.api.util.CheckInResponseComparator;
import com.capair.api.util.maps.CustomerMapper;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private CustomerDao customerDao;

    @Autowired 
    private ItineraryDao itineraryDao;

    @Autowired
    private UserCustomerDao userCustomerDao;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired 
    private JwtGen jwtGen;

    @Autowired 
    private ItineraryService itineraryService;
    
    @Override
    public boolean existsAlready(String email) {

       if(userDao.existsByEmail(email)){
         return true;
       }
        return false;
    }

    @Override
    public void addUser(RegisterRequest request) {
        
        User newUser = new User();
        newUser.setCustomer(null); //per frontent we want this to be null at first until we set their information
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        
        userDao.save(newUser);

    }

    @Override
    public User getUser(String email) {
        return userDao.findByEmail(email);
    }

    @Override
    public boolean passwordMatches(String password, String passwordFromUser){
        return passwordEncoder.matches(password, passwordFromUser);
    }

    @Override
    public String logout() {
       
        ResponseCookie cookie = jwtGen.getCleanJwtCookie();
        
        return cookie.toString();
        
    }

    @Override
    public LoginResponse authenticate(LoginDetailsRequest loginDetails) {

        String email = loginDetails.getEmail();
        String password = loginDetails.getPassword();

        Authentication authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(email, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        ResponseCookie jwtCookie = jwtGen.generateJwtCookie(userDetails);

        String jwt = jwtCookie.toString();

        jwt = getJwtFromCookie(jwt);
        
        //return proper information
        return new LoginResponse(jwt, (int)jwtCookie.getMaxAge().getSeconds());

    }

    private String getJwtFromCookie(String cookie){

        int equalsIndex = cookie.indexOf("="); 
        int semicolonIndex = cookie.indexOf(";"); 

        if (equalsIndex != -1 && semicolonIndex != -1 && equalsIndex < semicolonIndex) {
            return cookie.substring(equalsIndex + 1, semicolonIndex).trim();
        }

        return null; 

    }

    public UserDetailsResponse getUserDetails(String jwt){

        UserDetailsResponse userDetailsResponse = new UserDetailsResponse();
        
        String username = jwtGen.getUserNameFromJwtToken(jwt);

        User user = userDao.findByEmail(username);

        if(user == null){
            throw new NotFoundException("User not found");
        }

        if(user.getCustomer() != null){

            Customer customer = customerDao.findById(user.getCustomer().getCustomerId()).get();
            
            userDetailsResponse = customerMapper.customerToDetailedCustomerResponse(customer);

            userDetailsResponse.setCustomerId(customer.getCustomerId());
            userDetailsResponse.setCustomerCreated(true);
        }
        else{
            userDetailsResponse.setCustomerCreated(false);
            userDetailsResponse.setEmail(username);
            userDetailsResponse.setCustomerId(null);
        }
         
        return userDetailsResponse;
    }
    
    public UserFlightsResponse getUserFlights(String jwt){
        
        UserFlightsResponse userFlightsResponse = new UserFlightsResponse();

        String username = jwtGen.getUserNameFromJwtToken(jwt);
        User user = userDao.findByEmail(username);

        if(user == null){

            throw new NotFoundException("User not found");

        }
        List<Integer> itineraryIds = new ArrayList<Integer>();
        List<CheckInResponse> flightDetails = new ArrayList<CheckInResponse>();
        
        itineraryIds = itineraryDao.findByItinerariesForUser(user.getId());

        if(itineraryIds.size() == 0){
            userFlightsResponse.setAllTrips(null);
        }

        for(Integer itinerary : itineraryIds){

            flightDetails.add(itineraryService.checkInFlight(new CheckInRequest(itinerary, "bypassme1234")));
            
        }

        Collections.sort(flightDetails, new CheckInResponseComparator());
        userFlightsResponse.setAllTrips(flightDetails);
        
        return userFlightsResponse;
    }

    @Override
    public String updateUser(UserUpdateRequest req, String jwt) {
 
        String username = jwtGen.getUserNameFromJwtToken(jwt);

        User user = userDao.findByEmail(username);

        if(user == null){
            throw new NotFoundException("User not found");
        }

        Customer customer = null;
        
        if(user.getCustomer() != null){
            customer = user.getCustomer();
        }
     
        //set user details
        if(req.getPassword() != null){
            user.setPassword(passwordEncoder.encode(req.getPassword()));
        }

        if(req.getEmail() != null && customerDao.findByEmail(req.getEmail()) != null){
           
            throw new BadRequestException("email exists already");
        }

        if(customer == null){
            Customer updatedCustomer = customerMapper.UpdatedCustomerToCustomer(req);
            customerDao.save(updatedCustomer);

            UserCustomer uc = new UserCustomer(user, updatedCustomer);
            userCustomerDao.save(uc);

            user.setCustomer(updatedCustomer);

            userDao.save(user);
        }
        else{
            //set customer details
            CustomerMapper.updateUser(user, req, username);
            userDao.save(user);
        }
        
        return "Successfully Updated";        
    }

    @Override
    public void updateItinerary(LoginDetailsRequest loginDetails) {
        Optional<Itinerary> optItinerary = itineraryDao.findById(loginDetails.getItineraryId());

        if(optItinerary.isEmpty()){
            throw new NotFoundException("Itinerary with id:" + loginDetails.getItineraryId() + " not found");
        }

        Itinerary itinerary = optItinerary.get();
        itinerary.setUserId(userDao.findByEmail(loginDetails.getEmail()).getId());

        itineraryDao.save(itinerary);
    }

    @Override
    public String addPassenger(Passenger passenger, String jwt) {
        User user = userDao.findByEmail(jwtGen.getUserNameFromJwtToken(jwt));
        if (user == null){
            throw new UnauthorizedException("Jwt token is not valid");
        }

        Customer customer = customerDao.findByEmail(passenger.getEmail());
        if (customer == null){
            customer = customerMapper.passengerToCustomer(passenger);
            if (customer == null){
                throw new BadRequestException("Passenger:" + passenger.toString() + " is not valid");
            }
            customerDao.save(customer);
        }
        if (!userCustomerDao.existsByUserIdAndCustomerCustomerId(user.getId(), customer.getCustomerId())){
            userCustomerDao.save(new UserCustomer(user, customer));
        }
        else {
            throw new BadRequestException("Passenger has already been added to account");
        }
        
        return new StringBuilder()
                    .append("Passenger added successfully")
                    .toString();
    }

    @Override
    public UserPassengerResponse getUserPassengers(String jwt){
        String username = jwtGen.getUserNameFromJwtToken(jwt);

        User user = userDao.findByEmail(username);

        List<Integer> customers = userCustomerDao.findByUserId(user.getId());
        if(customers.size() == 0){
            throw new NotFoundException(username+ " does not have more passengers");
        }

        List<UserPassenger> passengers = new ArrayList<UserPassenger>();
        for(Integer i: customers){
            Customer c1 = customerDao.findById(i).get();

            if(c1 != null){
                UserPassenger cr1 = customerMapper.customerToUserPassenger(c1);
                passengers.add(cr1);
            }
            else{
                throw new NotFoundException("not found");
            }

        }

        UserPassengerResponse multiplePassengerResponse = new UserPassengerResponse();
        multiplePassengerResponse.setCustomers(passengers);

        return multiplePassengerResponse;

    }

    @Override
    public String changePassword(PasswordRequest passwords, String jwt){
        String username = jwtGen.getUserNameFromJwtToken(jwt);

        User user = userDao.findByEmail(username);

        if(user == null){
            throw new NotFoundException("User not found");
        }

        if(this.passwordMatches(passwords.getOldPassword(), user.getPassword())){
            user.setPassword(passwordEncoder.encode(passwords.getNewPassword()));
            userDao.save(user);
        }
        else{
            throw new UnauthorizedException("Current password is incorrect");
        }
    
        return "Successfully changed password";

    }

}
