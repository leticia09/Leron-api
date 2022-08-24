package com.leron.api.model;

import com.leron.api.commons.TypeCardEnum;
import com.leron.api.commons.TypeFinancialAgentEnum;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_cartao")
public class FinancialAgentModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "tipo")
    private TypeFinancialAgentEnum type;

    @Column(name = "cartao_id")
    private Long cardId;
}
