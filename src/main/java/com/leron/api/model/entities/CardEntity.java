package com.leron.api.model.entities;

import com.leron.api.commons.TypeCardEnum;
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
public class CardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id_cartao")
    private String userId;

    @Column(name = "nome_cartao")
    private String cardName;

    @Column(name = "tipo")
    private TypeCardEnum type;

    @Column(name = "final")
    private Long endCard;

    @Column(name = "agente_financeiro_id")
    private Long financialAgentId;

    @Column(name = "data_vencimento")
    private Date dueDate;
}
