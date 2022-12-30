package com.elijah.onlinebankingapp.repository.bank;

import com.elijah.onlinebankingapp.model.account.BankAccount;
import com.elijah.onlinebankingapp.model.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount,Long> {
    BankAccount findByCustomer(Customer customer);
    BankAccount findByAccountNumber(String accountNumber);
}
