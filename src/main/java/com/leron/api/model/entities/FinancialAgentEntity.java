package com.leron.api.model.entities;

import com.leron.api.commons.TypeFinancialAgentEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
}
