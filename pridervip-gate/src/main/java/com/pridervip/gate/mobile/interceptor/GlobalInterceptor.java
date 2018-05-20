/*
package com.pridervip.api.gate.mobile.interceptor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

//import com.bighuobi.api.gate.service.mobile.MobileCaptchaService;
import com.bighuobi.api.gate.utils.Constants;
import com.bighuobi.api.gate.utils.JsonUtil;
import com.bighuobi.api.gate.utils.RedisUtils;

*/
/**
 * Created by Administrator on 2014/11/28.
 *//*

public class GlobalInterceptor implements HandlerInterceptor {

	private static final Logger logger = LoggerFactory
			.getLogger(GlobalInterceptor.class);

	@Resource
	private ExceptionUtils exceptionUtils;

	@Resource
	private RedisUtils redisUtils;

//	@Resource
//	private MobileCaptchaService mobileCaptchaService;

	@Resource
	private MessageSource messageSource;

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		logger.debug("preHandle······servlet path:{}", request.getServletPath());
		logger.debug("path will be replace with:{}", request.getServletPath()
				.replaceAll("/", "-"));
//		用于记录请求时间
        request.setAttribute("startTime",System.currentTimeMillis());  
		String path = request.getServletPath();
		if (path.equals("/mobile/user/login")) {
			return preValidLogin(request, response);
		} else if (path.equals("/mobile/user/logout")) {
			return true;
		} else if (path.equals("/mobile/futures/codes")) {
			return true;
		} else if (path.equals("/mobile/exchange.do")) {
			return true;
		} else if (path.equals("/mobile/user/verifyga")) {
			return true;
		} else if (path.equals("/mobile/user/captcha.jpg")) {
			return true;
		} else if (path.equals("/push/mobile/bindcid2userid")) {
			return true;
		} else if (path.equals("/push/addpushmessage")) {
			return true;
		} else {
			String token = request.getParameter("token");
			if (StringUtils.isEmpty(token)) {
				throw exceptionUtils.getMobileCustomerException(-4);
			}
			// 获取userid
			Object object = redisUtils.get(6, Constants.LOGIN_KEY + token);
			if (object == null) {
				throw exceptionUtils.getMobileCustomerException(-7);
			} else {
				Object lastToken = redisUtils.get(6, Constants.LOGIN_VERIFY_KEY
						+ object);
				if (lastToken != null && !token.equals(lastToken.toString())) {
					throw exceptionUtils.getMobileCustomerException(-9);
				}
				request.setAttribute("userid", object);
				return true;
			}
		}
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		logger.debug("postHandle······");
		long startTime = (Long)request.getAttribute("startTime");  
        request.removeAttribute("startTime");  
        long timeCost=System.currentTimeMillis()-startTime;
        logger.info("the timeCost:{}",timeCost);
        if(timeCost>100){//处理请求超过100毫秒，打印日志
            logParameters(request,false);
        }
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		logger.debug("afterCompletion······");
	}

	private boolean preValidLogin(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String email = request.getParameter("email");
		Object object = redisUtils.get(6, Constants.LOGIN_ERROR_COUNT_KEY + email);// 获取该token的失败登陆次数
		if (object != null
				&& Integer.valueOf(object.toString()) >= Constants.NEED_CAPTCHA_LOGIN_FAILED_COUNT) {
			String captcha = request.getParameter("captcha");
			String sessionid = request.getParameter("sessionid");
			if (StringUtils.isEmpty(captcha)) {
				sessionid = UUID.randomUUID().toString();
				response.getWriter().write(createCapcha(sessionid));
				response.flushBuffer();
				return false;
				// throw exceptionUtils.getMobileCustomerException(-8); // 需要验证码
			} else {
				if(StringUtils.isEmpty(sessionid)) {
					throw exceptionUtils.getMobileCustomerException(-10); // 验证码错误
				}
				object = redisUtils.get(6, Constants.CAPTCHA_REDIS_KEY + sessionid);
				logger.info("用户输入的captcha:{}, 系统保存的captcha:{}", captcha, object);
				if (object != null && object.toString().equals(captcha)) {
					return true;
				} else {
					logger.info("系统验证码错误");
					// 返回验证码错误
					throw exceptionUtils.getMobileCustomerException(-3); // 验证码错误
				}
			}
		} else {
			return true;
		}
	}

	private String createCapcha(String sessionid) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", -8);
		map.put("url", "/mobile/user/captcha.jpg?sessionid="+sessionid);
		map.put("sessionid", sessionid);
		return JsonUtil.toJson(map);
	}
	
	protected void logParameters(HttpServletRequest request, boolean isError) {
        String uri = request.getRequestURI();
        Map<String, String[]> parameterMap = request.getParameterMap();
        StringBuffer sb = new StringBuffer();
        sb.append("LONGTIME request uri = " + uri + " || ");
        Set<String> keySet = parameterMap.keySet();
        if(keySet != null)
        for (String key : keySet) {
            if(key !=null){
                sb.append(key + "=");
                sb.append(request.getParameter(key));
                sb.append(",");
            }
        }
        if(isError){
            logger.error(sb.toString());
        } else {
            logger.info(sb.toString());
        }
    }

}*/
