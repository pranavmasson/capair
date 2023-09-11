package com.capair.api.util.maps;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.capair.api.model.Customer;
import com.capair.api.model.User;
import com.capair.api.model.request.Passenger;
import com.capair.api.model.request.UserUpdateRequest;
import com.capair.api.model.response.CustomerResponse;
import com.capair.api.model.response.UserDetailsResponse;
import com.capair.api.model.response.UserPassenger;
import com.capair.api.util.DateUtils;

@Mapper(componentModel = "spring", uses=DateUtils.class)
public interface CustomerMapper {
    
    CustomerResponse customerToCustomerResponse(Customer customer);

    @Mapping(target="birthDate", source="customer.birthDate",
            dateFormat="yyyy-MM-dd")
    @Mapping(target="customerCreated", ignore=true)
    UserDetailsResponse customerToDetailedCustomerResponse(Customer customer);
    
    List<CustomerResponse> customerToCustomerResponse(List<Customer> customers);

    @Mapping(target="birthDate", source="passenger.birthDate",
    dateFormat="yyyy-MM-dd")
    @Mapping(target="customerId", ignore=true)
    @Mapping(target="zipcode", ignore=true)
    @Mapping(target="city", ignore=true)
    @Mapping(target="gender", ignore=true)
    @Mapping(target="address", ignore=true)
    @Mapping(target="state", ignore=true)
    Customer updateCustomerFromRequest(@MappingTarget Customer customer, Passenger passenger);

    @Mapping(target="birthDate", source="passenger.birthDate",
            dateFormat="yyyy-MM-dd")
    @Mapping(target="customerId", ignore=true)
    @Mapping(target="zipcode", ignore=true)
    @Mapping(target="city", ignore=true)
    @Mapping(target="gender", ignore=true)
    @Mapping(target="address", ignore=true)
    @Mapping(target="state", ignore=true)
    Customer passengerToCustomer(Passenger passenger);

    @Mapping(target="birthDate", source="req.birthDate",
             dateFormat="yyyy-MM-dd")
    @Mapping(target="customerId", ignore=true)
    @Mapping(target="zipcode", ignore=true)
    @Mapping(target="city", ignore=true)
    @Mapping(target="gender", ignore=true)
    @Mapping(target="address", ignore=true)
    @Mapping(target="state", ignore=true)
    Customer UpdatedCustomerToCustomer(UserUpdateRequest req);

    @Mapping(target="birthDate", source="req.birthDate",
    dateFormat="yyyy-MM-dd")
    UserPassenger customerToUserPassenger(Customer req);

    static void updateUser(User user, UserUpdateRequest req, String username){
        if(req.getFirstName() != null){
            user.getCustomer().setFirstName(req.getFirstName());
        }

        if(req.getLastName() != null){
            user.getCustomer().setLastName(req.getLastName());
        }

        if(req.getPhoneNumber() != null){
            user.getCustomer().setPhoneNumber(req.getPhoneNumber());
        }

        if(req.getBirthDate()!= null){
            user.getCustomer().setBirthDate(req.getBirthDate());
        }

        if(req.getEmail() != null && !req.getEmail().equals(username)){
            user.getCustomer().setEmail(req.getEmail());
        }
    }
}


