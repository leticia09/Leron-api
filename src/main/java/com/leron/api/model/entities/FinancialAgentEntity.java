package com.leron.api.model.entities;

import com.leron.api.commons.TypeFinancialAgentEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_cartao")
public class FinancialAgentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name_agente_financeiro")
    private String name;

    @Column(name = "tipo")
    private TypeFinancialAgentEnum type;

    @Column(name = "cartao_id")
    private Long cardId;

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
