package com.leron.api.model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_card")
public class CardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "card_name")
    private String cardName;

    @Column(name = "status")
    private String status;

    @Column(name = "modality")
    private String modality;

    @Column(name = "final_card")
    private Long finalCard;

    @Column(name = "bank_id")
    private Long bankId;

    @Column(name = "due_date")
    private Long dueDate;
}
