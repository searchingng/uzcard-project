package com.company.uzcard.service;

import com.company.uzcard.dto.CardDTO;
import com.company.uzcard.entity.CardEntity;
import com.company.uzcard.entity.ProfileEntity;
import com.company.uzcard.entity.TransactionEntity;
import com.company.uzcard.repository.CardRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.net.PortUnreachableException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CardService {

    private final CardRepository cardRepository;
//    private final ProfileService profileService;

    public CardEntity create(CardEntity entity){
        cardRepository.save(entity);
        return entity;
    }

    public CardDTO create(CardDTO dto){
        CardEntity entity = new CardEntity();
        LocalDate expDate = LocalDate.parse(dto.getExpDate() + "/01",
                DateTimeFormatter.ofPattern("MM/yy/dd"));

        Long balance = dto.getBalance() == null ? 0L : dto.getBalance();

        entity.setNumber(dto.getNumber());
        entity.setExpDate(expDate);
        entity.setBalance(balance);
        entity.setProfileId(dto.getProfileId());

        cardRepository.save(entity);
        dto.setId(entity.getId());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    public Page<CardDTO> getAll(Pageable pageable){
        return cardRepository.findAll(pageable)
                .map(this::toDto);
    }

    public CardEntity get(Long id){
        if (id == null) {
            log.warn("Card Not Found, Id is null");
            return null;
        }

        return cardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Card Not Found"));
    }

    public CardEntity get(String number){
        if (number == null) {
            log.warn("Card Not Found, Number is Null");
            return null;
        }

        return cardRepository.findByNumber(number)
                .orElseThrow(() -> new RuntimeException("Card Not Found"));
    }

    public CardDTO getById(Long id){
        return toDto(get(id));
    }

    public List<CardEntity> getByProfileId(Long id){
        if (id == null)
            return null;

        log.info("Profile's cards, profileId = {}", id);
        return cardRepository.findByProfileId(id);
    }

    public CardDTO findByNumberAndExpDate(CardDTO dto){
        String expDate = dto.getExpDate() + "/01";
        LocalDate exp = LocalDate.parse(expDate, DateTimeFormatter.ofPattern("MM/yy/dd"));
        return cardRepository.findByNumberAndExpDate(dto.getNumber(), exp)
                .map(this::toDto).orElseThrow(() -> new RuntimeException("Such card Not Exist"));
    }

    public String deleteById(Long id){
        cardRepository.deleteById(id);
        return "Successfully Deleted!!!";
    }

    public CardDTO getByNumber(String number){
        return toDto(get(number));
    }

    public CardEntity create(String number, LocalDate expDate, Long profileId){
//        ProfileEntity profile = profileService.get(profileId);

        CardEntity card = new CardEntity();
        card.setNumber(number);
        card.setExpDate(expDate);
        card.setBalance(0L);
        card.setProfileId(profileId);
//        card.setProfile(profile);
        cardRepository.save(card);

        return card;
    }

    public Long getBalanceByNumber(String number){
        return cardRepository.getBalanceByNumber(number);
    }

    @Transactional
    public void updateBalance(String number, Long amount) {
        cardRepository.updateBalance(number, amount);
    }

    private CardDTO toDto(CardEntity entity) {
        if (entity == null)
            return null;
        CardDTO dto = new CardDTO();
        dto.setExpDate(entity.getExpDate()
                .format(DateTimeFormatter.ofPattern("MM/yy")));

        dto.setNumber(entity.getNumber());
        dto.setBalance(entity.getBalance());
        dto.setProfileId(entity.getProfileId());
        dto.setId(entity.getId());
        dto.setCreatedAt(entity.getCreatedAt());

        return dto;
    }
}
