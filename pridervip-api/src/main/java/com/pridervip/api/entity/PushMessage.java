package com.pridervip.api.entity;

import java.io.Serializable;

public class PushMessage implements Serializable {

    private static final long serialVersionUID = -5482823050812948225L;

    public Integer id;
    public String recv_id;
    public Integer recv_id_type;
    public Integer task_index;
    public Integer device_type;
    public Integer action_type;
    public String source;
    public String title;
    public String content;
    public String url;
    public Integer status;
    public Integer try_times;
    public Integer time_push;
    public Integer time_create;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRecvId() {
        return recv_id;
    }

    public void setRecvId(String recv_id) {
        this.recv_id = recv_id;
    }

    public Integer getRecvIdType() {
        return recv_id_type;
    }

    public void setRecvIdType(Integer recv_id_type) {
        this.recv_id_type = recv_id_type;
    }

    public Integer getTaskIndex() {
        return task_index;
    }

    public void setTaskIndex(Integer task_index) {
        this.task_index = task_index;
    }
    
    public Integer getDeviceType() {
        return device_type;
    }

    public void setDeviceType(Integer device_type) {
        this.device_type = device_type;
    }
    
    public Integer getActionType() {
        return action_type;
    }

    public void setActionType(Integer action_type) {
        this.action_type = action_type;
    }
    
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    
    
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
    
    public Integer getTryTimes() {
        return try_times;
    }

    public void setTryTimes(Integer try_times) {
        this.try_times = try_times;
    }
    
    public Integer getTimePush() {
        return time_push;
    }

    public void setTimePush(Integer time_push) {
        this.time_push = time_push;
    }

    public Integer getTimeCreate() {
        return time_create;
    }

    public void setTimeCreate(Integer time_create) {
        this.time_create = time_create;
    }
}
