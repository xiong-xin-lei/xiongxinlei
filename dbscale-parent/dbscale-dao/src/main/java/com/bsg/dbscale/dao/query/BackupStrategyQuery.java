package com.bsg.dbscale.dao.query;

import java.io.Serializable;

public class BackupStrategyQuery implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String servGroupId;
    private Boolean enabled;
    private String siteId;

    public String getServGroupId() {
        return servGroupId;
    }

    public void setServGroupId(String servGroupId) {
        this.servGroupId = servGroupId;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    @Override
    public String toString() {
        return "BackupStrategyQuery [servGroupId=" + servGroupId + ", enabled=" + enabled + ", siteId=" + siteId + "]";
    }

}
