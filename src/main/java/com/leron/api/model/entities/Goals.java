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
public class Goals extends GenericEntities {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String goal;
    private Long ownerId;
    private Long bankId;
    private Long accountId;
    private Timestamp openDate;
    private Integer validityInMonths;
    private BigDecimal value;
    private String currency;
    private String profitabilityMonthly;
    private String goalPreference;
    private BigDecimal partValue;
}
