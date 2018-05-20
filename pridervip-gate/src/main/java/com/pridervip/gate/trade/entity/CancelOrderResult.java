package com.pridervip.gate.trade.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2014/12/1.
 */
public class CancelOrderResult implements Serializable {

    private static final long serialVersionUID = 1291421023130792609L;

    private String result;

    public CancelOrderResult() {
        super();
    }

    public CancelOrderResult(boolean cancelResult) {
        if (cancelResult) {
            this.result = "success";
        } else {
            this.result = "failed";
        }
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
