package com.bighuobi.api.gate.trade.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2014/11/25.
 */
public class ErrorInfo implements Serializable {

    private static final long serialVersionUID = 5229779197743910614L;

    private int code;

    private String message;

    public ErrorInfo() {
    }

    public ErrorInfo(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
