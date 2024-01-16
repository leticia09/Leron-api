package com.leron.api.model.entities;

import com.leron.api.model.GenericEntities;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Forecast extends GenericEntities {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long ownerId;
    private BigDecimal value;
    private Long macroGroupId;
    private Long specificGroupId;
    private String currency;
    private Boolean hasFixed;

    @ElementCollection
    @CollectionTable(name = "forecast_months", joinColumns = @JoinColumn(name = "forecast_id"))
    @Column(name = "month")
    private List<String> months;

    @ElementCollection
    @CollectionTable(name = "forecast_years", joinColumns = @JoinColumn(name = "forecast_id"))
    @Column(name = "year")
    private List<Integer> years;
}
