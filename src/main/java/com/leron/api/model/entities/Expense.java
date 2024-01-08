package com.leron.api.model.entities;

import com.leron.api.model.GenericEntities;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Expense extends GenericEntities {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String local;
    private String macroGroup;
    private String specificGroup;
    private Long ownerId;
    private String paymentForm;
    private Long bankId;
    private Long accountId;
    private Long finalCard;
    private Long quantityPart;
    private Boolean hasFixed;
    private Timestamp dateBuy;
    private String obs;
    private BigDecimal value;
    private Long financialEntityId;
    private Long financialEntityCardId;
    private Long moneyId;
    private Boolean hasSplitExpense;
    private String frequency;
    private Timestamp initialDate;
    private Long monthPayment;
    private Long dayPayment;
    private String currency;
}
