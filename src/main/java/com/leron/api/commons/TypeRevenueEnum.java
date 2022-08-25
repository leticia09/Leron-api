package com.leron.api.commons;

import lombok.Getter;

@Getter
public enum TypeRevenueEnum {
    SALARY(1, "SALÁRIO"),
    INVESTMENT(2, "INVESTIMENTO");

    private int key;
    private String value;

    TypeRevenueEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }
}
