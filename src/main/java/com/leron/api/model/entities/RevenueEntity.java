package com.leron.api.model.entities;

import com.leron.api.commons.TypeRevenueEnum;
import com.leron.api.model.GenericEntities;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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

    @Column(name = "name")
    private String name;

    @Column(name = "financial_agent_id")
    private Long financialAgentId;

    @Column(name = "type")
    private TypeRevenueEnum type;

    @Column(name = "status")
    private Boolean status = Boolean.FALSE;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private String endDate;
}
