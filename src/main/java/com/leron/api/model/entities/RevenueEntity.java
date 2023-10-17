package com.leron.api.model.entities;

import com.leron.api.commons.TypeRevenueEnum;
import com.leron.api.model.GenericEntities;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_revenue")
public class RevenueEntity extends GenericEntities {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "id_salary", nullable = false)
    private Long salaryId;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "receivingDate", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date receivingDate;

    @Column(name = "value", nullable = false)
    private BigDecimal value;

    @Column(name = "id_bank", nullable = false)
    private Long bankId;

    @Column(name = "id_account", nullable = false)
    private Long accountId;
}
