package com.leron.api.model.entities;

import com.leron.api.commons.FormPaymentEnum;
import com.leron.api.commons.ParcelEnum;
import com.leron.api.commons.TypeShoppingEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_compras")
public class ShoppingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_compras")
    private String shoppingName;

    @Column(name = "valor_compras")
    private BigDecimal shoppingValue;

    @Column(name = "cartao_id_compras")
    private Long shoppingCardId;

    @Column(name = "forma_pagamento")
    private FormPaymentEnum formPayment;

    @Column(name = "parcela")
    private ParcelEnum parcel;

    @Column(name = "numero_parcela")
    private Long userId;

    @Column(name = "quantidade_parcela")
    private Long amountParcel;

    @Column(name = "valor_parcela")
    private BigDecimal valueParcel;

    @Column(name = "local_compra")
    private String place;

    @Column(name = "data_compra")
    private Date shoppingDate;

    @Column(name = "tipo")
    private TypeShoppingEnum shoppingType;

    @Column(name = "observacao")
    private String obs;

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
