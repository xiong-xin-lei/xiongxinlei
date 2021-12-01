package com.bsg.dbscale.service.query;

import java.io.Serializable;

public class ServGroupQuery implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String siteId;
    private String category;
    private Boolean createSuccess;
    private String state;

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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
        return "ServGroupQuery [siteId=" + siteId + ", category=" + category + ", createSuccess=" + createSuccess
                + ", state=" + state + "]";
    }

}
