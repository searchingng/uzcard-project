package com.company.uzcard.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionDTO {

    private String id;
    private LocalDateTime createdAt;

    private Long fromCardId;
    private Long toCardId;

    private String fromNumber;
    private String toNumber;

    private Long amount;

}
