package com.bsg.dbscale.dbscale.query;

import java.io.Serializable;

public class NetworkQuery implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String siteId;
    private String businessAreaId;
    private String topology;
    private Boolean enabled;

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getBusinessAreaId() {
        return businessAreaId;
    }

    public void setBusinessAreaId(String businessAreaId) {
        this.businessAreaId = businessAreaId;
    }

    public String getTopology() {
        return topology;
    }

    public void setTopology(String topology) {
        this.topology = topology;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "NetworkQuery [siteId=" + siteId + ", businessAreaId=" + businessAreaId + ", topology=" + topology
                + ", enabled=" + enabled + "]";
    }

}
