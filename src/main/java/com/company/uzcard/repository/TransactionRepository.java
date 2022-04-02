package com.company.uzcard.repository;

import com.company.uzcard.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionEntity, String>,
        JpaSpecificationExecutor<TransactionEntity> {

    // By Profile
    @Query("SELECT t FROM transaction t WHERE " +
            "t.fromCard.profileId = ?1 OR t.toCard.profileId = ?1")
    List<TransactionEntity> findByProfileId(Long profileId);

    @Query("SELECT t FROM transaction t WHERE " +
            "t.fromCard.profileId = ?1 ORDER BY t.createdAt DESC")
    List<TransactionEntity> findCreditByProfileId(Long profileId);

    @Query("SELECT t FROM transaction t WHERE " +
            "t.toCard.profileId = ?1 ORDER BY t.createdAt DESC")
    List<TransactionEntity> findDebitByProfileId(Long profileId);


    // By Card
    @Query("SELECT t FROM transaction t WHERE " +
            "t.fromCardId = ?1 OR t.toCardId = ?1")
    List<TransactionEntity> findByCardId(Long cardId);

    @Query("SELECT t FROM transaction t WHERE " +
            "t.fromCardId = ?1 ORDER BY t.createdAt DESC")
    List<TransactionEntity> findCreditByCardId(Long cardId);

    @Query("SELECT t FROM transaction t WHERE " +
            "t.toCardId = ?1 ORDER BY t.createdAt DESC")
    List<TransactionEntity> findDebitByCardId(Long cardId);


}
