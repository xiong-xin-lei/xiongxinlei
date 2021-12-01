package com.bsg.dbscale.dbscale.query;

import java.io.Serializable;

public class HostQuery implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String siteId;
    private String cluterId;
    private String state;

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getCluterId() {
        return cluterId;
    }

    public void setCluterId(String cluterId) {
        this.cluterId = cluterId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "HostQuery [siteId=" + siteId + ", cluterId=" + cluterId + ", state=" + state + "]";
    }

}
