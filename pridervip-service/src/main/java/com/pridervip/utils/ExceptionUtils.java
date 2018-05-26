/*
package com.pridervip.api.utils;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import com.bighuobi.api.exception.CustomerException;

*/
/**
 * Created by Administrator on 2014/11/19.
 *//*

@Component
public class ExceptionUtils {

    @Resource
    private MessageSource messageSource;

    */
/**
     * message = messageSource.getMessage("error.#{错误代码}", null, null)
     *
     * @param code 错误代码
     * @return new CustomerException(错误信息, 错误代码)
     *//*

    public CustomerException getCustomerException(int code) {
        String message = messageSource.getMessage("error." + code, null, null);
        return new CustomerException(message, code, ExceptionType.SPOT);
    }
    
    public CustomerException getCustomerException(int code, String message) {
        return new CustomerException(message, code, ExceptionType.SPOT);
    }
    public CustomerException getCustomerException(int code, String message, boolean dynamic) {
        return new CustomerException(message, code, ExceptionType.SPOT, dynamic);
    }
    public CustomerException getCustomerException(int code,Map<String,Object> dataMap) {
        String message = messageSource.getMessage("error." + code, null, null);
        return new CustomerException(message, code, ExceptionType.SPOT,dataMap);
    }
    
    
    

    public CustomerException getFuturesCustomerException(int code) {
        String message = messageSource.getMessage("futures_error." + code, null, null);
        return new CustomerException(message, code, ExceptionType.FUTURES);
    }
    public CustomerException getFuturesCustomerException(int code,String extMessage) {
        String message = messageSource.getMessage("futures_error." + code, null, null);
        return new CustomerException(message, code, ExceptionType.FUTURES);
    }

}
*/
