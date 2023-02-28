package com.leron.api.model.entities;

import com.leron.api.commons.TypeRevenueEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_receita")
public class RevenueEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_receita")
    private String name;

    @Column(name = "agente_financeiro_id")
    private Long financialAgentId;

    @Column(name = "tipo")
    private TypeRevenueEnum type;

    @Column(name = "status")
    private Boolean status = Boolean.FALSE;

    @Column(name = "data_inicio")
    private Date startDate;

    @Column(name = "nome")
    private String endDate;

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
