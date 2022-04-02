package com.company.uzcard.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.tomcat.jni.Local;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import java.time.LocalDate;

@Entity(name = "card")
@Setter
@Getter
@NoArgsConstructor
public class CardEntity extends BaseEntity{

    @Column(nullable = false, unique = true, length = 16)
    private String number;

    private Long balance;

    private LocalDate expDate;

    @Column(name = "profile_id", nullable = false)
    private Long profileId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", insertable = false, updatable = false)
    private ProfileEntity profile;

    @Override
    public String toString() {
        return "Card{" +
                "number='" + number + '\'' +
                ", balance=" + balance +
                ", expDate=" + expDate +
                ", profileId=" + profileId +
                '}';
    }
}
