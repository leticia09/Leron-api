package com.leron.api.model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForecastDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String month;
    private String currency;
    private BigDecimal value;
    private Long year;
    private Boolean deleted = Boolean.FALSE;

    @ManyToOne
    @JoinColumn(name = "forecast_id")
    @Where(clause = "deleted = false")
    private Forecast forecast;
}
