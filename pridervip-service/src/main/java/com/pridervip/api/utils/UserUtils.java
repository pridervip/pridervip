/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. All Rights Reserved
 */
package com.pridervip.api.utils;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 */
@Component
public class UserUtils {

    private static Logger logger = LoggerFactory.getLogger(UserUtils.class);

    private static Map<String, String> userInfoMap = new HashMap<String, String>();
    private static List<String> keysList = new ArrayList<String>();
    private static Random random = new Random();

    /**
     * init huobi user
     */
    static {
        ResourceBundle appBundle = PropertyResourceBundle.getBundle("META-INF/conf/user-conf");
        Enumeration<String> enumeration = appBundle.getKeys();
        String key = StringUtils.EMPTY;
        int i = 0;
        while (enumeration.hasMoreElements()) {
            key = enumeration.nextElement();
            userInfoMap.put(key, appBundle.getString(key));
            keysList.add(key);
            ++i;
        }
        logger.info("共加载用户{}个,用户信息:{}", i, userInfoMap);
    }

    /**
     * 获取随机用户
     *
     * @return access_key&secret_key by random
     */
    public String getKeys() {
        String key = keysList.get(random.nextInt(keysList.size()));
        logger.info("随机获取用户,用户index:{}", key);
        return getKeys(key);
    }

    /**
     * 获取指定的用户
     *
     * @return access_key&secret_key by key
     */
    public String getKeys(String key) {
        return new StringBuffer().append(key).append("=").append(userInfoMap.get(key)).toString();
    }

}
