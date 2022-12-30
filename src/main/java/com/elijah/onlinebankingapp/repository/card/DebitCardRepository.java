package com.elijah.onlinebankingapp.repository.card;

import com.elijah.onlinebankingapp.model.account.BankAccount;
import com.elijah.onlinebankingapp.model.card.DebitCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DebitCardRepository extends JpaRepository<DebitCard,Long> {
    DebitCard findByBankAccount(BankAccount bankAccount);
}
