package com.pridervip.api.entity;

import java.io.Serializable;

public class PushPriceNotice implements Serializable {

    private static final long serialVersionUID = -5482823050812948224L;
    

    public Integer id;
    public Integer user_id;
    public Integer device_type;
    public String cid;
    public String device_token;
    public String symbol_id;
    public Integer task_index;
    public Long price;
    public Integer currency;
    public String lang;
    public Integer price_type;
    public Integer price_condition;
    public Integer status;
    public Integer time_last_push;
    public Integer time_create;
    

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

    public String getSymbolId() {
        return symbol_id;
    }

    public void setSymbolId(String symbol_id) {
        this.symbol_id = symbol_id;
    }

    public Integer getTaskIndex() {
        return task_index;
    }

    public void setTaskIndex(Integer task_index) {
        this.task_index = task_index;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Integer getCurrency() {
        return currency;
    }

    public void setCurrency(Integer currency) {
        this.currency = currency;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public Integer getPriceType() {
        return price_type;
    }

    public void setPriceType(Integer price_type) {
        this.price_type = price_type;
    }

    public Integer getPriceCondition() {
        return price_condition;
    }

    public void setPriceCondition(Integer price_condition) {
        this.price_condition = price_condition;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
    
    public Integer getTimeLastPush() {
        return time_last_push;
    }

    public void setTimeLastPush(Integer time_last_push) {
        this.time_last_push = time_last_push;
    }

    public Integer getTimeCreate() {
        return time_create;
    }

    public void setTimeCreate(Integer time_create) {
        this.time_create = time_create;
    }
}
