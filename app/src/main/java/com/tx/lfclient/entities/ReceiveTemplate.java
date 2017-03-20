package com.tx.lfclient.entities;

import java.io.Serializable;

/**
 * Created by (IMP)郑和明
 * Date is 2017/2/15
 */
public class ReceiveTemplate<T> implements Serializable {


    private String token = "LF";
    private T data;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ReceiveTemplate{" +
                "token='" + token + '\'' +
                ", data=" + data +
                '}';
    }
}
