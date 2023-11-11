package com.leron.api.model.entities;

import com.leron.api.model.GenericEntities;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "tb_score_transfer")
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
    private BigDecimal originValue;

    @Column(name = "destinyValue", nullable = false)
    private BigDecimal destinyValue;

    @Column(name = "program")
    private BigDecimal bonus;

    @Column(name = "ownerIdOrigin")
    private Long ownerIdOrigin;

    @Column(name = "ownerIdDestiny")
    private Long ownerIdDestiny;
}
