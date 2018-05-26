/*
 * huobi.com Inc.
 * Copyright (c) 2012 火币天下
 */
package com.pridervip.gate.utils;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 获取用户IP工具类
 * @author yanjg
 * 
 * @creation 2012-12-3
 */
public class RemortIPUtil {
    
    private static Logger logger = LoggerFactory.getLogger(RemortIPUtil.class);
//    将长Ip截取
    public static String getRemortIP(HttpServletRequest request) {
        if (request.getHeader("x-forwarded-for") == null) {
            return request.getRemoteAddr();
        }
        // 获得方向代理IP
        String ip = request.getHeader("x-forwarded-for");
        if (StringUtils.isNotBlank(ip)) {
            if (ip.indexOf(",") > -1) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }

        return ip;
    }
    
    public static String getRemortIPLong(HttpServletRequest request) {
            String ip = request.getHeader("x-forwarded-for"); 
            if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
                ip = request.getHeader("Proxy-Client-IP"); 
            } 
            if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
                ip = request.getHeader("WL-Proxy-Client-IP"); 
            } 
            if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
                ip = request.getRemoteAddr(); 
            } 
            return ip; 
    }
    
    public static String getRealIp(HttpServletRequest req) {
        String regex = "([0-9]{1,3}.){3}[0-9]{1,3}";
        String ip = req.getRemoteAddr();
        if(null != req.getHeader("HTTP_CDN_SRC_IP") && Pattern.matches(regex, req.getHeader("HTTP_CDN_SRC_IP"))) {
            ip = req.getHeader("HTTP_CDN_SRC_IP");
        }else if(null != req.getHeader("X-FORWARDED-FOR") && Pattern.matches(regex, req.getHeader("X-FORWARDED-FOR"))) {
            ip = req.getHeader("X-FORWARDED-FOR");
        }else if(null != req.getHeader("X-REAL-IP") && Pattern.matches(regex, req.getHeader("X-REAL-IP"))) {
            ip = req.getHeader("X-REAL-IP");
        }else if(null != req.getHeader("HTTP_X_REAL_FORWARDED_FOR") && Pattern.matches(regex, req.getHeader("HTTP_X_REAL_FORWARDED_FOR"))) {
            ip = req.getHeader("HTTP_X_REAL_FORWARDED_FOR");
        }else if(null != req.getHeader("HTTP_X_FORWARDED_FOR") && Pattern.matches(regex, req.getHeader("HTTP_X_FORWARDED_FOR"))) {
            ip = req.getHeader("HTTP_X_FORWARDED_FOR");
        }else if(null != req.getHeader("HTTP_X_REAL_IP") && Pattern.matches(regex, req.getHeader("HTTP_X_REAL_IP"))) {
            ip = req.getHeader("HTTP_X_REAL_IP");
        }else if(null != req.getHeader("HTTP_CLIENT_IP") && Pattern.matches(regex, req.getHeader("HTTP_CLIENT_IP"))) {
            ip = req.getHeader("HTTP_CLIENT_IP");
        }
        return ip;
    }
    
    public static Long ip2Long(String ip) {
        String regex = "([0-9]{1,3}.){3}[0-9]{1,3}";
        if(Pattern.matches(regex, ip)) {
            String[] ips = ip.split("\\."); 
            Long v = Long.valueOf(ips[0]);
            Long v1 = Long.valueOf(ips[1]);
            Long v2 = Long.valueOf(ips[2]);
            Long v3 = Long.valueOf(ips[3]);
            return (v << 24) + (v1 << 16) + (v2 << 8) + v3;
        }
        return null;
    }

}
