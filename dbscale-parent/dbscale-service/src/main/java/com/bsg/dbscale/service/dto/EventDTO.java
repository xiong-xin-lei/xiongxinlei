package com.bsg.dbscale.service.dto;

import java.io.Serializable;

public class EventDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String type;
    private String reason;
    private String interval;
    private String message;
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
        return "EventDTO [type=" + type + ", reason=" + reason + ", interval=" + interval + ", message=" + message
                + ", source=" + source + "]";
    }

}
