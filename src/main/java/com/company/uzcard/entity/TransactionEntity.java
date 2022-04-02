package com.company.uzcard.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "transaction")
@Setter
@Getter
@NoArgsConstructor
public class TransactionEntity extends UUIDEntity{

    @ManyToOne
    @JoinColumn(name = "from_card_id", insertable = false, updatable = false)
    private CardEntity fromCard;

    @ManyToOne
    @JoinColumn(name = "to_card_id", insertable = false, updatable = false)
    private CardEntity toCard;

    @Column(name = "from_card_id")
    private Long fromCardId;

    @Column(name = "to_card_id")
    private Long toCardId;

    private Long amount;


}
