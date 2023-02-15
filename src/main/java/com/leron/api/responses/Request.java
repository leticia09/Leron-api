package com.leron.api.responses;

import java.io.Serializable;

public class Request implements Serializable {
    private static final long serialVersionUID = 5475411755679503065L;
    private String locale;
    private String authorization;

    public Request() {
    }

    public Request(String locale) {
        this.locale = locale;
    }

    public Request(String locale, String authorization) {
        this.locale = locale;
        this.authorization = authorization;
    }

    public String getLocale() {
        return this.locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getAuthorization() {
        return this.authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }
}