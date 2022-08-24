package com.leron.api.model;

import com.leron.api.commons.TypeCardEnum;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_cartao")
public class CardModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario")
    private String user;

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
