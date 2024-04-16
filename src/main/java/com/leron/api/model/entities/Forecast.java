package com.leron.api.model.entities;

import com.leron.api.model.GenericEntities;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

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
    private Long macroGroupId;
    private Long specificGroupId;
    private String macroGroupName;
    private String specificGroupName;

    @OneToMany(mappedBy = "forecast", cascade = CascadeType.ALL)
    @Where(clause = "deleted = false")
    List<ForecastDate> forecastDates;

}
