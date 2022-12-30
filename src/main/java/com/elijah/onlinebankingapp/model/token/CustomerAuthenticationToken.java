package com.elijah.onlinebankingapp.model.token;

import com.elijah.onlinebankingapp.model.customer.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@AllArgsConstructor
public class CustomerAuthenticationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Date createdDate;
    private String token;

    @OneToOne(targetEntity = Customer.class,fetch = FetchType.EAGER)
    @JoinColumn(nullable = false,name = "customer_id")
    private Customer customer;

    public CustomerAuthenticationToken(Customer customer){
        this.customer = customer;
        this.createdDate = new Date();
        this.token = UUID.randomUUID().toString();
    }
}
