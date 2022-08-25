package com.leron.api.model.entities;

import com.leron.api.commons.TypeBankTransitionEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_movimentacao_bancaria")
public class BankTransitionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id_movimentacao")
    private String userId;

    @Column(name = "nome_movimentacao")
    private String transitionName;

    @Column(name = "data_movimentacao")
    private Date transitionDate;

    @Column(name = "tipo_movimentacao")
    private TypeBankTransitionEnum transitionType;

    @Column(name = "agente_financeiro_id_movimentacao")
    private Long transitionFinancialAgentId;

    @Column(name = "receita_id_movimentacao")
    private Long revenueIdTransition;

    @Column(name = "valor")
    private Double value;



}
