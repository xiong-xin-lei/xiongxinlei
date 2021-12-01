package com.bsg.dbscale.dbscale.query;

import java.io.Serializable;

public class BusinessSubsystemQuery implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String businessSystemId;
    private Boolean enabled;

    public String getBusinessSystemId() {
        return businessSystemId;
    }

    public void setBusinessSystemId(String businessSystemId) {
        this.businessSystemId = businessSystemId;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "BusinessSubsystemQuery [businessSystemId=" + businessSystemId + ", enabled=" + enabled + "]";
    }

}
