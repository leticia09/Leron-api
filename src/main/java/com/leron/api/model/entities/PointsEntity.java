package com.leron.api.model.entities;

import com.leron.api.model.GenericEntities;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_points")
public class PointsEntity extends GenericEntities {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "program", nullable = false)
    private String program;

    @Column(name = "id_bank")
    private Long bankId;

    @Column(name = "id_account")
    private Long accountId;

    @Column(name = "currency")
    private String currency;

    @Column(name = "point")
    private BigDecimal point;

    @Column(name = "status", nullable = false)
    private String status;
}
