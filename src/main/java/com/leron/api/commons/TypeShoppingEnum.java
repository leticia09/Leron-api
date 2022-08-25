package com.leron.api.commons;

import lombok.Getter;

@Getter
public enum TypeShoppingEnum {
    HELP(1,"AJUDA"),
    FOOD(2,"ALIMENTAÇÃO"),
    BEAUTY(3,"BELEZA"),
    DEBT(4,"CONTAS"),
    TENTH(5,"DÍZIMO"),
    EDUCATION(6,"EDUCAÇÃO"),
    LOAN(7,"EMPRESTIMO"),
    TAXATION(8,"IMPOSTO"),
    LOUNGE(9,"LAZER"),
    HABITATION(10,"MORADIA"),
    GIFT(11,"PRESENTE"),
    PHARMACY(12,"FARMÁCIA"),
    JOB(13,"TRABALHO"),
    TRANSPORT(14,"TRANSPORTE"),
    CLOTHING(15,"VESTUÁRIO"),
    SUPERMARKET(16,"SUPERMERCADO");

    private int key;
    private String value;


    TypeShoppingEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }
}
