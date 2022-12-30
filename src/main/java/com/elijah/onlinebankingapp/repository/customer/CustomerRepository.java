package com.elijah.onlinebankingapp.repository.customer;

import com.elijah.onlinebankingapp.model.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Integer> {
    Customer findByEmail(String email);
    Customer findByPhoneNumber(String phoneNumber);
}
