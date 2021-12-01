package com.bsg.dbscale.dao.query;

import java.io.Serializable;

public class BusinessAreaQuery implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String siteId;
    private Boolean enabled;

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "BusinessAreaQuery [siteId=" + siteId + ", enabled=" + enabled + "]";
    }

}
