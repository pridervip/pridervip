package com.bighuobi.api.gate.utils;

import com.bighuobi.api.exception.CustomerException;
import com.bighuobi.api.gate.trade.entity.ErrorInfo;
import com.huobi.exception.HuobiException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2014/11/27.
 */
@Component
public class TradeExceptionResolver {

    private static final Logger logger = LoggerFactory.getLogger(TradeExceptionResolver.class);

    @Resource
    private MessageSource messageSource;

    public ErrorInfo handleException(HttpServletRequest request, HttpServletResponse response, Exception e) {
        Long flag = System.currentTimeMillis();
        int code = 0;
        String message = StringUtils.EMPTY;
        if (e instanceof CustomerException) {
            code = ((CustomerException) e).getCode();
            message = messageSource.getMessage("error." + code, null, request.getLocale());
        } else if (e instanceof HuobiException) {
            code = ((HuobiException) e).getCode();
            message = e.getMessage();
        }else if (e instanceof HttpRequestMethodNotSupportedException) {
            code = 63;
            message = messageSource.getMessage("error.63", null, request.getLocale());
        } else if (e instanceof MissingServletRequestParameterException) {
            code = 65;
            message = messageSource.getMessage("error.65", null, request.getLocale()) + " '"
                    + ((MissingServletRequestParameterException) e).getParameterName() + "'";
        } else if (e instanceof HttpMediaTypeNotSupportedException) {
            code = 66;
            message = messageSource.getMessage("error.66", null, request.getLocale()) + " '"
                    + ((HttpMediaTypeNotSupportedException) e).getContentType() + "'";
        } else {
            logger.error("{}:发生错误:{}", flag, e);
            code = 1;
            message = messageSource.getMessage("error.1", null, request.getLocale());
            ExceptionQueue.exceptionQueue.offer(e);
        }
        ErrorInfo errorInfo = new ErrorInfo(code, message);
        return errorInfo;
    }

}
