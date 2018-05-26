package com.pridervip.gate.utils;

import java.math.BigDecimal;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 */
public class Constants {

    private static ResourceBundle appBundle = PropertyResourceBundle.getBundle("conf/application");
    
    private static ResourceBundle appBundle_2 = PropertyResourceBundle.getBundle("conf/ga");
    
    public static final String BTC_MAX_TRADE_MONEY = appBundle.getString("btc_max_trade_money");

    public static final String BTC_MIN_TRADE_MONEY = appBundle.getString("btc_min_trade_money");

    public static final String LTC_MAX_TRADE_MONEY = appBundle.getString("ltc_max_trade_money");

    public static final String LTC_MIN_TRADE_MONEY = appBundle.getString("ltc_min_trade_money");

    public static final long CREATED_TIMEL_IMITED = Long.parseLong(appBundle.getString("created_time_limited"));

    /**
     * redis保存登录失败次数
     */
    public static final String LOGIN_ERROR_COUNT_KEY = "login_error_count";

    /**
     * 验证码在redis保存的key
     */
    public static final String CAPTCHA_REDIS_KEY = "captcha_key";
    
    /**
     * 验证LoginGA redis保存的key
     */
//    public static final String LoginGA_TOKEN_REDIS_KEY = "loginga_key";
    /**
     * 登录session 保存的进redis 用于ga 验证通过时 保存时长
     */
    public static final String Login_SESSION_REDIS_KEY = "session_key";
    
    /**
     * redis保存登录token-userid 的key
     */
    public static final String LOGIN_KEY = "login_token_key";
    /**
     * redis保存登录userid-token 的key 用于失效验证提示
     */
    public static final String LOGIN_VERIFY_KEY = "login_verify_key";
    /**
     * mobile_data_version
     */
    public static final String MOBILE_DATA_VERSION = appBundle.getString("mobile_data_version");

    /**
     * 交易API请求参数created最大允许偏差
     */
    public static final Long CREATED_TIME_LIMITED = Long.parseLong(appBundle.getString("created_time_limited"));

    /**
     * 超过该次数需要验证码
     */
    public static final Integer NEED_CAPTCHA_LOGIN_FAILED_COUNT = Integer.parseInt(appBundle.getString("need_captcha_login_failed_count"));

    /**
     * 超过该次数暂时锁定用户
     */
    public static final Integer LOCK_USER_LOGIN_FAILED_COUNT = Integer.parseInt(appBundle.getString("lock_user_login_failed_count"));

    /**
     * Mobile Home Code
     */
    public static final String MOBILE_HOME_CODE = appBundle.getString("mobile_home_code");
    /**
     * 验证码失效时间
     */
    public static final Long CAPCHA_SESSION = Long.valueOf(appBundle.getString("captcha_session"));
    /**
     * token 失效时间  10 days
     */
    public static final Long TOKEN_SESSION = Long.valueOf(appBundle.getString("token_session"));
    /**
     * token 6 hours
     */
    public static final Long TOKEN_SESSION_H6 = Long.valueOf(appBundle.getString("token_session_6h"));
    
    /**
     * loginga
     */
    public static final Boolean OPEN_LOGINGA = Boolean.valueOf(appBundle.getString("open_loginga"));
    
    public static String GA_DOMAIN = appBundle_2.getString("ga.url");
    public static String GA_IP = appBundle_2.getString("ga.ip");
//    public static String GA_KEY = appBundle_2.getString("ga.key");
//    public static String GA_ADMINKEY = appBundle_2.getString("ga.adminkey");
//    public static Boolean REMOTE = Boolean.valueOf(appBundle_2.getString("remote"));
    /**
     * 期货最小交易量
     * @param key
     * @return
     */
    public static final BigDecimal MIN_TRADE_AMOUNT=new BigDecimal("0.0001");

    public static String getMessage(String key) {
    	return appBundle.getString(key);
    }
    public static final String PROXY_HOST = appBundle.getString("proxy.host");
    public static final Integer PROXY_PORT = Integer.parseInt(appBundle.getString("proxy.port"));
    public static final String PROXY_PROTOCOL = appBundle.getString("proxy.protocol");
    public static final String PROXY_HOSTS_INCLUDE = appBundle.getString("proxy.hosts.include");
}
