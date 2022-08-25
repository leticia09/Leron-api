package com.leron.api.commons;

import lombok.Getter;

@Getter
public enum TypeFinancialAgentEnum {
    INVESTMENT(1, "INVESTIMENTO"),
    BANK(2, "BANCO");

    private int key;
    private String value;

    TypeFinancialAgentEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }
}