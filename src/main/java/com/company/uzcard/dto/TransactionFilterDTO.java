package com.company.uzcard.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TransactionFilterDTO {

    private String id;
    private Long profileId;
    private String cardNumber;
    private Long cardId;
    private Long fromAmount;
    private Long toAmount;
    private LocalDate fromDate;
    private LocalDate toDate;

}
