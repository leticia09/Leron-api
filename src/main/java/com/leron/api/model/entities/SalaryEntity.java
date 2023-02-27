package com.leron.api.model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_salary")
public class SalaryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name= "name", nullable = false)
    private String name;

    @Column(name= "type", nullable = false)
    private String type;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name= "deleted")
    private Boolean deleted = Boolean.FALSE;

    @Column(name = "CREATED_IN")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date createdIn;

    @Column(name = "CREATED_BY")
    private Long createdBy;

    @Column(name = "CHANGED_IN")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date changedIn;

    @Column(name = "CHANGED_BY")
    private Long changedBy;
}
