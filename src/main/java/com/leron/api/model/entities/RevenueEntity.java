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

}
