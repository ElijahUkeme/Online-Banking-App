package com.elijah.onlinebankingapp.repository.card;

import com.elijah.onlinebankingapp.model.card.CardType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardTypeRepository extends JpaRepository<CardType,Long> {
}
