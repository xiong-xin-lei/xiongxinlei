package com.bsg.dbscale.service.dto;

import java.io.Serializable;

public class TerminalDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String scheme;
    private String addr;
    private String sessionId;

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public String toString() {
        return "TerminalDTO [scheme=" + scheme + ", addr=" + addr + ", sessionId=" + sessionId + "]";
    }

}
