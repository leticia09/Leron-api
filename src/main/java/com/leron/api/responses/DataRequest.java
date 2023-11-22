package com.leron.api.responses;

public class DataRequest<T> extends Request {
    private static final long serialVersionUID = -9199226445117528353L;
    private T data;

    public DataRequest() {
    }

    public DataRequest(T object) {
        this.data = object;
    }

    public DataRequest(T object, String locale, String authorization) {
        this.data = object;
        this.setLocale(locale);
        this.setAuthorization(authorization);
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }
}