package com.company.uzcard.repository;

import com.company.uzcard.entity.CardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<CardEntity, Long> {

    Optional<CardEntity> findByNumber(String number);

    List<CardEntity> findByProfileId(Long id);

    @Modifying
    @Transactional
    @Query("UPDATE card as c SET c.balance = c.balance + ?2 WHERE c.number = ?1")
    void updateBalance(String number, Long amount);

    @Query("SELECT c.balance FROM card as c WHERE c.number = ?1")
    Long getBalanceByNumber(String number);

}
