package com.capair.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="user_customer")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCustomer {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="user_customer_id")
    private int userCustomerId;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name="user_id")
    private User user;

    
    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name="customer_id")
    private Customer customer;  

    public UserCustomer (User user, Customer customer) {
        this.user = user;
        this.customer = customer;
    }

}
