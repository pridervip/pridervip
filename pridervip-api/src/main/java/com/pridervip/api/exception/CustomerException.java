package com.bighuobi.api.exception;

import java.util.Map;

import com.bighuobi.api.enums.ExceptionType;

/**
 * @author yanjg
 * 2015年1月26日
 */
public class CustomerException extends Exception {
    private static final long serialVersionUID = 5860353203820291969L;

    private Integer code;
    private Map<String,Object> data;

    private ExceptionType exceptionType;
    /**
     * 错误信息中format {}中的信息 ，默认为false
     */
    private Boolean isFormat = false;

    public CustomerException(Integer code, ExceptionType exceptionType) {
        this.code = code;
        this.exceptionType = exceptionType;
    }

    public CustomerException(String message, Integer code, ExceptionType exceptionType) {
        super(message);
        this.code = code;
        this.exceptionType = exceptionType;
    }

    public CustomerException(String message, Throwable cause, Integer code, ExceptionType exceptionType) {
        super(message, cause);
        this.code = code;
        this.exceptionType = exceptionType;
    }

    public CustomerException(Throwable cause, Integer code, ExceptionType exceptionType) {
        super(cause);
        this.code = code;
        this.exceptionType = exceptionType;
    }

    public CustomerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Integer code, ExceptionType exceptionType) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
        this.exceptionType = exceptionType;
    }
    
    public CustomerException(String message, Integer code, ExceptionType exceptionType, boolean isFormat) {
        super(message);
        this.code = code;
        this.exceptionType = exceptionType;
        this.isFormat = isFormat;
    }

    /**
     * 将Map也返回给前端
     * @param message
     * @param code2
     * @param spot
     * @param dataMap
     */
    public CustomerException(String message, int code, ExceptionType exceptionType, Map<String, Object> dataMap) {
        super(message);
        this.code = code;
        this.exceptionType = exceptionType;
        this.data=dataMap;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public ExceptionType getExceptionType() {
        return exceptionType;
    }

    public void setExceptionType(ExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

	public Boolean getIsFormat() {
		return isFormat;
	}

	public void setIsFormat(Boolean isFormat) {
		this.isFormat = isFormat;
	}

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
