package com.bighuobi.api.gate.mobile.entity;

import java.io.Serializable;

/**
 * 返回对象父类
 */
public class BaseResult implements Serializable {

    private static final long serialVersionUID = -322244269519055746L;

    private Integer code;

    private String message;

    /**
     * @param status
     * @param message
     */
    public BaseResult(int status, String message) {
        this.code=status;
        this.message=message;
    }
    

    /**
     * 
     */
    public BaseResult() {
        super();
        // TODO Auto-generated constructor stub
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public Integer getCode() {
        return code;
    }


    public void setCode(Integer code) {
        this.code = code;
    }
}
