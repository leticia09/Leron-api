package com.leron.api.model.entities;

import com.leron.api.model.GenericEntities;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_card")
public class CardEntity extends GenericEntities {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nick_name", nullable = false)
    private String nickName;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "modality", nullable = false)
    private String modality;

    @Column(name = "final_card", nullable = false)
    private Long finalCard;

    @Column(name = "bank_id", nullable = false)
    private Long bankId;

    @Column(name = "bill_close", nullable = false)
    private Long billClose;

    @Column(name = "due_date", nullable = false)
    private Long dueDate;

}
