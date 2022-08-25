package com.leron.api.commons;

import lombok.Getter;

@Getter
public enum TypeBankTransitionEnum {

    DEPOSIT(1, "DEPOSITO"),
    LOAN(2, "EMPRESTIMO"),
    PAYMENT(3, "PAGAMENTO"),
    YIELD(4, "RENDIMENTO"),
    WITHDRAW(5, "SAQUE"),
    TRANSFER_SENT(6, "TRANSFERÊNCIA ENVIADA"),
    TRANSFER_RECEIVED(7, "TRANSFERÊNCIA RECEBIDA");

    private int key;
    private String value;

    TypeBankTransitionEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }
}
