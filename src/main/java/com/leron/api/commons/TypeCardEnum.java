package com.leron.api.commons;

import lombok.Getter;

@Getter
public enum TypeCardEnum {
    CREDIT(1,"Crédito"),
    DEBIT(2,"Débito"),
    CREDIT_DEBIT(3,"Crédito/Débito");

    private int key;
    private String value;

    TypeCardEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }
}
