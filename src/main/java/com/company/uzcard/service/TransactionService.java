package com.company.uzcard.service;

import com.company.uzcard.dto.TransactionDTO;
import com.company.uzcard.dto.TransactionFilterDTO;
import com.company.uzcard.entity.BaseEntity;
import com.company.uzcard.entity.CardEntity;
import com.company.uzcard.entity.TransactionEntity;
import com.company.uzcard.repository.TransactionRepository;
import com.company.uzcard.spec.SpecificationBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.rowset.Predicate;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CardService cardService;

    public TransactionEntity create(TransactionEntity entity){
        transactionRepository.save(entity);
        return entity;
    }

    public TransactionDTO save(TransactionDTO dto){

        TransactionEntity entity = new TransactionEntity();
        entity.setFromCardId(dto.getFromCardId());
        entity.setToCardId(dto.getToCardId());
        entity.setAmount(dto.getAmount());

        transactionRepository.save(entity);
        dto.setId(entity.getId());
        dto.setCreatedAt(entity.getCreatedAt());

        return dto;

    }

    public TransactionDTO makeTransaction(TransactionDTO dto){
        CardEntity fromCard = null;
        CardEntity toCard = null;
        if (dto.getFromCardId() != null){
            fromCard = cardService.get(dto.getFromCardId());
        } else
            fromCard = cardService.get(dto.getFromNumber());

        if (dto.getToCardId() != null) {
            toCard = cardService.get(dto.getToCardId());
        } else
            toCard = cardService.get(dto.getToNumber());

        return doTransaction(fromCard, toCard, dto.getAmount());

    }



    @Transactional
    public TransactionDTO makeTransaction(String fromNumber, String toNumber, Long amount){

        CardEntity fromCard = cardService.get(fromNumber);
        CardEntity toCard = cardService.get(toNumber);

        return doTransaction(fromCard, toCard, amount);
    }

    public TransactionDTO doTransaction(CardEntity fromCard, CardEntity toCard, Long amount){
        Long balance = cardService.getBalanceByNumber(fromCard.getNumber());
        if (balance < amount){
            log.warn("Not enough money in {}", fromCard);
            throw new RuntimeException("Not enough balance");
        }

        cardService.updateBalance(fromCard.getNumber(), amount * -1);
        cardService.updateBalance(toCard.getNumber(), amount);

        TransactionEntity transaction = new TransactionEntity();
        transaction.setFromCardId(fromCard.getId());
        transaction.setToCardId(toCard.getId());
        transaction.setAmount(amount);

        transactionRepository.save(transaction);
        TransactionDTO transactionDto = toDto(transaction);
        log.info("New transaction: {}", transactionDto);
        return transactionDto;
    }

    public TransactionDTO toDto(TransactionEntity entity){
        if (entity == null)
            return null;

        TransactionDTO dto = new TransactionDTO();
        dto.setId(entity.getId());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setFromCardId(entity.getFromCardId());
        dto.setToCardId(entity.getToCardId());
        dto.setAmount(entity.getAmount());

        return dto;
    }

    // 4. APIs

    public List<TransactionDTO> findByProfileId(Long profileId){
        log.debug("Transactions of Profile: id = {}", profileId);
        return transactionRepository.findByProfileId(profileId)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<TransactionDTO> findCreditByProfileId(Long profileId){
        log.debug("Credits of Profile: id = {}", profileId);
        return transactionRepository.findCreditByProfileId(profileId)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<TransactionDTO> findDebitByProfileId(Long profileId){
        log.debug("Debits of Profile: id = {}", profileId);
        return transactionRepository.findDebitByProfileId(profileId)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<TransactionDTO> findByCardId(Long cardId){
        log.debug("Transactions of Card: id = {}", cardId);
        return transactionRepository.findByCardId(cardId)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<TransactionDTO> findCreditByCardId(Long cardId){
        log.debug("Credits of Card: id = {}", cardId);
        return transactionRepository.findCreditByCardId(cardId)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<TransactionDTO> findDebitByCardId(Long cardId){
        log.debug("Debits of Card: id = {}", cardId);
        return transactionRepository.findDebitByCardId(cardId)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    public Page<TransactionDTO> filter(TransactionFilterDTO dto, Pageable pageable){
        SpecificationBuilder<TransactionEntity> builder =
                new SpecificationBuilder<TransactionEntity>("id");

        Specification<TransactionEntity> spec = builder
                .equal("id", dto.getId())
                .greaterThanOrEqualTo("amount", dto.getFromAmount())
                .lessThanOrEqualTo("amount", dto.getToAmount())
                .fromDate("createdAt", dto.getFromDate())
                .toDate("createdAt", dto.getToDate())
                .build();

        if (dto.getProfileId() != null){
            List<CardEntity> cards = cardService.getByProfileId(dto.getProfileId());

            spec = spec.and((root, query, criteriaBuilder) ->
                criteriaBuilder.or(root.get("fromCard").in(cards),
                        root.get("toCard").in(cards))
            );
        }

        CardEntity card = dto.getCardId() == null ? null : cardService.get(dto.getCardId());
        if (dto.getCardNumber() != null){
            card = cardService.get(dto.getCardNumber());
        }

        if (card != null){
            CardEntity cardEntity = card;
            spec = spec.and((root, query, criteriaBuilder) ->criteriaBuilder.or(
                            criteriaBuilder.equal(root.get("fromCardId"), cardEntity.getId()),
                            criteriaBuilder.equal(root.get("toCard"), cardEntity)
                    )
            );
        }
        log.debug("Filtering transactions");

        return transactionRepository.findAll(spec, pageable).map(this::toDto);

    }

}
