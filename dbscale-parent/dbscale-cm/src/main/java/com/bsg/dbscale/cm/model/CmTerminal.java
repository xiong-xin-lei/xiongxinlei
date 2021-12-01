package com.bsg.dbscale.cm.model;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

public class CmTerminal implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @JSONField(name = "session_id")
    private String sessionId;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public String toString() {
        return "CmTerminal [sessionId=" + sessionId + "]";
    }

}
