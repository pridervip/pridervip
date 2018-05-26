package com.pridervip.entity;

import java.io.Serializable;
import java.math.BigDecimal;

public class Cid2UserId implements Serializable {

    private static final long serialVersionUID = -5482823050812948226L;

    public Integer id;
    public Integer user_id;
    public String cid;
    public String device_token;
    public Integer device_type;
    public Integer time_create;
    public Integer time_update;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return user_id;
    }

    public void setUserId(Integer user_id) {
        this.user_id = user_id;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getDeviceToken() {
        return device_token;
    }

    public void setDeviceToken(String device_token) {
        this.device_token = device_token;
    }
    
    public Integer getDeviceType() {
        return device_type;
    }

    public void setDeviceType(Integer device_type) {
        this.device_type = device_type;
    }
    
    public Integer getTimeCreate() {
        return time_create;
    }

    public void setTimeCreate(Integer time_create) {
        this.time_create = time_create;
    }
    
    public Integer getTimeUpdate() {
        return time_update;
    }

    public void setTimeUpdate(Integer time_update) {
        this.time_update = time_update;
    }
}
