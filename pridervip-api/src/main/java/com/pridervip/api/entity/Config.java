/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. All Rights Reserved
 */
package com.pridervip.api.entity;

import java.io.Serializable;

/**
 * @author 任培伟
 * @version 1.0.0
 */
public class Config implements Serializable {

    private static final long serialVersionUID = 6921523662975321257L;

    private String app;

    private String key;

    private String value;

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
