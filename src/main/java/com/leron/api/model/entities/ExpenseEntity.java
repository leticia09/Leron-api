package com.leron.api.model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_expense")
public class ExpenseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "nickName", nullable = false)
    private String nickName;

    @Column(name = "shopping_date", nullable = false)
    private java.util.Date shoppingDate;

    @Column(name = "local", nullable = false)
    private String local;

    @Column(name = "shopping_group", nullable = false)
    private String group;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "obs", nullable = false)
    private String obs;

    @Column(name = "form_payment", nullable = false)
    private String formPayment;

    @Column(name = "payer", nullable = false)
    private String payer;

    @Column(name = "card_id", nullable = false)
    private Long cardId;

    @Column(name = "method", nullable = false)
    private String method;

    @Column(name = "type_payment", nullable = false)
    private String typePayment;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "payment_date", nullable = false)
    private String paymentDate;

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
