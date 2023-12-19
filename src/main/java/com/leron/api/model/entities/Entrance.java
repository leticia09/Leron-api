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
public class Entrance extends GenericEntities {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String source;
    private String type;
    private Long ownerId;
    private BigDecimal salary;
    private Long bankId;
    private Long accountId;
    private String frequency;
    private Timestamp initialDate;
    private Timestamp finalDate;
    private Long monthReceive;
    private Long dayReceive;
    private Long financialEntityId;
}
