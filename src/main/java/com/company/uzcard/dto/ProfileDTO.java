package com.company.uzcard.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileDTO {

    private Long id;
    private String name;
    private String surname;
    private String middleName;
    private String phone;
    private LocalDate birthDate;
    private LocalDateTime createdAt;

}
