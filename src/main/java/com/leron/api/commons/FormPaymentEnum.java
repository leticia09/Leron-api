package com.leron.api.commons;

import lombok.Getter;

@Getter
public enum FormPaymentEnum {

    CREDIT(1,"CREDITO"),
    DEBIT(2,"DEBITO"),
    CASH(3,"A VISTA");

    private int key;
    private String value;

    FormPaymentEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }
}
