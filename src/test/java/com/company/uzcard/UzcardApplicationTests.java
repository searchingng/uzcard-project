package com.company.uzcard;

import com.company.uzcard.entity.CardEntity;
import com.company.uzcard.entity.ProfileEntity;
import com.company.uzcard.service.CardService;
import com.company.uzcard.service.ProfileService;
import com.company.uzcard.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
class UzcardApplicationTests {

    @Autowired
    private ProfileService profileService;
    @Autowired
    private CardService cardService;
    @Autowired
    private TransactionService transactionService;


    @Test
    void contextLoads() {
    }

    @Test
    void createProfile() {

        ProfileEntity profile = new ProfileEntity();
        profile.setName("Vali");
        profile.setSurname("Valiyev");
        profile.setMiddleName("Valihnovich");
        profile.setBirthDate(LocalDate.of(2001, 12, 22));

        profileService.save(profile);

    }

    @Test
    void createCard() {

        /*cardService.create(
                "8600123412341234",
                LocalDate.of(2026, 7, 1),
                1L);*/

//        cardService.increaseBalance("8600123412341234", 5000_00L);

    }

    @Test
    void testTransaction() {

        transactionService.makeTransaction("1234", "123123", 5000_00L);

    }

}
