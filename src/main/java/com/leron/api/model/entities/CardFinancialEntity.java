package com.leron.api.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.leron.api.model.GenericEntities;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardFinancialEntity extends GenericEntities {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cardName;
    private Long finalCard;
    private String modality;
    private BigDecimal balance;
    private Long ownerId;
    private String currency;

    @ManyToOne
    @JsonIgnore
    @ToString.Exclude
    @JoinColumn(name = "financialEntity_id")
    private FinancialEntity financialEntity;

}
