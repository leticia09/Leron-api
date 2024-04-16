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
public class Score extends GenericEntities {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "program", nullable = false)
    private String program;

    @Column(name = "value", nullable = false)
    private BigDecimal value;

    @Column(name = "points_expiration_date")
    private Timestamp pointsExpirationDate;

    @Column(name = "type_of_score")
    private String typeOfScore;

    @Column(name = "owner_id")
    private Long ownerId;
}
