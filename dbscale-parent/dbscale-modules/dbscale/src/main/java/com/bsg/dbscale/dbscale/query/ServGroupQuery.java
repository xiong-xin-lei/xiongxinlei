package com.bsg.dbscale.dbscale.query;

import java.io.Serializable;

public class ServGroupQuery implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String siteId;
    private Boolean createSuccess;
    private String state;

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public Boolean getCreateSuccess() {
        return createSuccess;
    }

    public void setCreateSuccess(Boolean createSuccess) {
        this.createSuccess = createSuccess;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "ServGroupQuery [siteId=" + siteId + ", createSuccess=" + createSuccess + ", state=" + state + "]";
    }

}
