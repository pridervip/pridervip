package com.bighuobi.api.gate.utils;

import com.bighuobi.api.enums.ExceptionType;
import com.bighuobi.api.exception.CustomerException;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2014/11/19.
 */
@Component
public class ExceptionUtils {

    @Resource
    private MessageSource messageSource;

    public CustomerException getCustomerException(int code) {
        String message = messageSource.getMessage("error." + code, null, null);
        return new CustomerException(message, code, ExceptionType.SPOT);
    }

    public CustomerException getFuturesCustomerException(int code) {
        String message = messageSource.getMessage("futures_error." + code, null, null);
        return new CustomerException(message, code, ExceptionType.FUTURES);
    }

    public CustomerException getMobileCustomerException(int code) {
        String message = messageSource.getMessage("mobile_error." + code, null, null);
        return new CustomerException(message, code, ExceptionType.MOBILE);
    }

}
