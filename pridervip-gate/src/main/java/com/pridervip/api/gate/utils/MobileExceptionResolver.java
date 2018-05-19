/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. All Rights Reserved
 */
package com.bighuobi.api.gate.utils;

import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bighuobi.api.enums.ExceptionType;
import com.bighuobi.api.exception.CustomerException;
import com.bighuobi.api.gate.mobile.entity.MobileData;
import com.huobi.exception.HuobiException;

/**
 * @author 任培伟
 * @version 1.0.0
 */
@ControllerAdvice
public class MobileExceptionResolver {

    private static Logger logger = LoggerFactory.getLogger(MobileExceptionResolver.class);

    @Resource
    private MessageSource messageSource;
    
//    @Resource
//	private OrderService orderService;
    
    @ExceptionHandler(Exception.class)
    public
    @ResponseBody
    MobileData handleException(Exception e, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Long flag = System.currentTimeMillis();
        MobileData mobileData = new MobileData();
        int status = 0;
        String message = StringUtils.EMPTY;
        String version = "";
        String path = request.getServletPath().replaceAll("/", "_");
        Map<String,Object> data=null;
        
        try {
			// 判断请求语言
			String lang = "";
			Locale localLang = Locale.CHINA;
			
			try {
				lang = request.getParameter("lang");
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
				lang = "";
			}
			
			if(lang == null || lang.equals(""))
			{
				localLang = Locale.CHINA;
			}
			else if(Locale.CHINA.getLanguage().equals(lang))
			{
				localLang = Locale.CHINA;
			}
			else if(Locale.US.getLanguage().equals(lang))
			{
				localLang = Locale.US;
			}
			else
			{
				localLang = Locale.CHINA;
			}
			
	    	String strTempMsg = e.getMessage();
	    	if(strTempMsg == null || strTempMsg.equals(""))
	    	{
	    		strTempMsg = "";
	    	}
			
			if (e instanceof CustomerException) {
			    status = ((CustomerException) e).getCode();
			    data=((CustomerException) e).getData();
			    
			    if (((CustomerException) e).getExceptionType().getValue() == ExceptionType.SPOT.getValue()) {
			        if(((CustomerException) e).getIsFormat()) {
			        	message = messageSource.getMessage("error." + status, new Object[]{e.getMessage()}, localLang);
			        }else{
			        	message = messageSource.getMessage("error." + status, null, localLang);
			        }
			        // 资金密码错误入库
			        if(status == 20) {
			        	insertChangeTradePwd(request);
			        }

			    } else if (((CustomerException) e).getExceptionType().getValue() == ExceptionType.FUTURES.getValue()) {
			    	
			        message = messageSource.getMessage("futures_error." + status, null, localLang);
			    } else if (((CustomerException) e).getExceptionType().getValue() == ExceptionType.MOBILE.getValue()) {
			    	if(strTempMsg != null && !strTempMsg.equals(""))
			    	{
						message = strTempMsg + ":" + messageSource.getMessage("mobile_error." + status, null, localLang);
			    	}
			    	else
			    	{
						message = messageSource.getMessage("mobile_error." + status, null, localLang);
			    	}
			    }
			   
			} else if (e instanceof HuobiException) {
				status = ((HuobiException) e).getCode();
			    message = e.getMessage();
			} else if (e instanceof HttpRequestMethodNotSupportedException) {
			    status = 63;
			    message = messageSource.getMessage("futures_error.63", null, localLang);
			} else if (e instanceof MissingServletRequestParameterException) {
			    status = 65;
			    message = messageSource.getMessage("futures_error.65", null, localLang) + " '"
			            + ((MissingServletRequestParameterException) e).getParameterName() + "'";
			} else if (e instanceof HttpMediaTypeNotSupportedException) {
			    status = 66;
			    message = messageSource.getMessage("futures_error.66", null, localLang) + " '"
			            + ((HttpMediaTypeNotSupportedException) e).getContentType() + "'";
			} else {
			    logger.error("{}:发生错误:{}", flag, e);
			    status = 1;
			    message = messageSource.getMessage("futures_error.1", null, localLang);
			    ExceptionQueue.exceptionQueue.offer(e);
			}
		} catch (Exception e1) {
			message = "unknown error:" + e1.getMessage();
		    logger.error("{}:发生错误:{}", flag, e1);
			e1.printStackTrace();
		}
        
        mobileData.setStatus(status);
        mobileData.setMessage(message);
        mobileData.setVersion(version);
        mobileData.setPath(path);
        mobileData.setData(data);
        return mobileData;
    }
    
    public void insertChangeTradePwd(HttpServletRequest request) {
//    	int userid = Integer
//				.parseInt(request.getAttribute("userid").toString());
//    	ChangeTradePasswordHistory ch = new ChangeTradePasswordHistory();
//		ch.setActionTime(System.currentTimeMillis() / 1000);
//		ch.setOperatorId(userid);
//		ch.setUserId(userid);
//		ch.setStatus(Byte.valueOf("0"));
//		ch.setType(Byte.valueOf("1"));
//		orderService.insertChange(ch);
    }
    

}
