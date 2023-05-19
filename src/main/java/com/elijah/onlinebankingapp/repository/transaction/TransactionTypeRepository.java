package com.elijah.onlinebankingapp.repository.transaction;

import com.elijah.onlinebankingapp.model.account.BankAccount;
import com.elijah.onlinebankingapp.model.transaction.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface TransactionTypeRepository extends JpaRepository<TransactionType,Long> {

    List<TransactionType> findByBankAccount(BankAccount bankAccount);

    @Query("select t from TransactionType t where t.bankAccount = :bankAccount and t.transactionDate >= :startDate AND t.transactionDate <= :endDate")
   List<TransactionType> findByBankAccountAndTransactionDate(BankAccount bankAccount, @Param("startDate") LocalDate startDate,@Param("endDate") LocalDate endDate);

    @Query("select t from TransactionType t where t.transactionDate >= :startDate and t.transactionDate <= :endDate")
    List<TransactionType> getTransactionStatement(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
