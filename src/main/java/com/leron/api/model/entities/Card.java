package com.leron.api.model.entities;

import com.leron.api.model.GenericEntities;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Card extends GenericEntities {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Long owner;
    private Long finalNumber;
    private String modality;
    private Integer closingDate;
    private Integer dueDate;
    private BigDecimal point;
    private String currency;
    private Long program;

    @ManyToOne
    @JoinColumn(name = "account_id")
    @Where(clause = "deleted = false")
    private Account account;
}
