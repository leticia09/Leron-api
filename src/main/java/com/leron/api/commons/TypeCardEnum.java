package com.leron.api.commons;

import lombok.Getter;

@Getter
public enum TypeCardEnum {
    CREDIT(1,"CREDITO"),
    DEBIT(2,"DEBITO"),
    CREDIT_DEBIT(3,"CREDITO&DEBITO");

    private int key;
    private String value;

    TypeCardEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }
}
