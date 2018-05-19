package com.bighuobi.api.gate.trade.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2014/12/1.
 */
public class SaveOrderResult implements Serializable {

    private static final long serialVersionUID = -1309404329386819709L;

    private Long id;

    private String result;

    public SaveOrderResult() {
        super();
    }

    public SaveOrderResult(Long id, String result) {
        this.id = id;
        this.result = result;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
