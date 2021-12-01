package com.bsg.dbscale.cm.model;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

public class CmEvent implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @JSONField(name = "type")
    private String type;

    @JSONField(name = "reason")
    private String reason;

    @JSONField(name = "interval")
    private String interval;

    @JSONField(name = "message")
    private String message;

    @JSONField(name = "source")
    private String source;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return "CmEvent [type=" + type + ", reason=" + reason + ", interval=" + interval + ", message=" + message
                + ", source=" + source + "]";
    }

}
