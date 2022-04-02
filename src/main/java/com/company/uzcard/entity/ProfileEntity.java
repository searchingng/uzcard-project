package com.company.uzcard.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity(name = "profile")
@Setter
@Getter
@NoArgsConstructor
public class ProfileEntity extends BaseEntity{

    private String name;

    private String surname;

    private String middleName;

    private LocalDate birthDate;


}
