package com.elijah.onlinebankingapp.repository.token;

import com.elijah.onlinebankingapp.model.customer.Customer;
import com.elijah.onlinebankingapp.model.token.CustomerAuthenticationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerTokenRepository extends JpaRepository<CustomerAuthenticationToken,Integer> {
    CustomerAuthenticationToken findByToken(String token);
    CustomerAuthenticationToken findByCustomer(Customer customer);
}
