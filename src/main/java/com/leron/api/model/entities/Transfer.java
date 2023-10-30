package com.leron.api.model.entities;

import com.leron.api.model.GenericEntities;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_points_transfer")
public class Transfer extends GenericEntities {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "originProgramId", nullable = false)
    private Long originProgramId;

    @Column(name = "destinyProgramId", nullable = false)
    private Long destinyProgramId;

    @Column(name = "quantity", nullable = false)
    private BigDecimal quantity;

    @Column(name = "pointsExpirationDate")
    private Timestamp pointsExpirationDate;

    @Column(name = "originValue", nullable = false)
    private BigDecimal originValue; // 1 para

    @Column(name = "destinyValue", nullable = false)
    private BigDecimal destinyValue; // 1

    @Column(name = "program")
    private BigDecimal bonus;
}
