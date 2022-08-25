package com.leron.api.commons;

import lombok.Getter;

@Getter
public enum ParcelEnum {

    PARCEL(1,"PARCELADO"),
    CASH(2,"A VISTA");

    private int key;
    private String value;

    ParcelEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }
}
