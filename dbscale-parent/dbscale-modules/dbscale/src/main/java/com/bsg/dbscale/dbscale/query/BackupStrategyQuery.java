package com.bsg.dbscale.dbscale.query;

import java.io.Serializable;

public class BackupStrategyQuery implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String siteId;
    private String servGroupId;

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getServGroupId() {
        return servGroupId;
    }

    public void setServGroupId(String servGroupId) {
        this.servGroupId = servGroupId;
    }

    @Override
    public String toString() {
        return "BackupStrategyQuery [siteId=" + siteId + ", servGroupId=" + servGroupId + "]";
    }

}
