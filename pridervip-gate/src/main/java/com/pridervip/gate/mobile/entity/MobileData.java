package com.pridervip.gate.mobile.entity;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2014/11/28.
 */
public class MobileData implements Serializable {

    private static final long serialVersionUID = -322244269519055746L;

    private Integer status;

    private String message;

    private String version;

    private String path;

    private Object data;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setData(Object data, String mapKey) {
        if (data instanceof List) {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            mapKey = StringUtils.isEmpty(mapKey) ? "list" : mapKey;
            dataMap.put(mapKey, data);
            this.data = dataMap;
        } else {
            this.data = data;
        }
    }
}
