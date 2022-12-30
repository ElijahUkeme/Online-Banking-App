package com.elijah.onlinebankingapp.service.token;

import com.elijah.onlinebankingapp.exception.DataNotFoundException;
import com.elijah.onlinebankingapp.model.customer.Customer;
import com.elijah.onlinebankingapp.model.token.CustomerAuthenticationToken;
import com.elijah.onlinebankingapp.repository.token.CustomerTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CustomerTokenService {

    @Autowired
    private CustomerTokenRepository customerTokenRepository;

    public void saveCustomerAuthenticationToken(CustomerAuthenticationToken customerAuthenticationToken){
        customerTokenRepository.save(customerAuthenticationToken);
    }
    public Customer getCustomerByToken(String token) throws DataNotFoundException {
        CustomerAuthenticationToken customerAuthenticationToken = customerTokenRepository.findByToken(token);
        if (Objects.isNull(customerAuthenticationToken)){
            throw new DataNotFoundException("Invalid Customer's token");
        }
        return customerAuthenticationToken.getCustomer();
    }
    public CustomerAuthenticationToken getTokenByCustomer(Customer customer){
        return customerTokenRepository.findByCustomer(customer);
    }
}
