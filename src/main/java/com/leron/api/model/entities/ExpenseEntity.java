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

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "shopping_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date shoppingDate;

    @Column(name = "local", nullable = false)
    private String local;

    @Column(name = "shopping_group", nullable = false)
    private String group;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "form_payment", nullable = false)
    private String formPayment;

    @Column(name = "payer", nullable = false)
    private String payer;

    @Column(name = "card_id")
    private Long cardId;

    @Column(name = "advance", nullable = false)
    private Boolean advance = Boolean.FALSE;

    @Column(name = "type_payment", nullable = false)
    private String typePayment;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "installment")
    private Long installment;

    @Column(name = "payment_date")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date paymentDate;

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
