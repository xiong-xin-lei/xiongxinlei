package com.bsg.dbscale.service.query;

import java.io.Serializable;

public class OrderGroupQuery implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String siteId;
    private String category;
    private String state;
    private String createType;
    private String owner;

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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCreateType() {
        return createType;
    }

    public void setCreateType(String createType) {
        this.createType = createType;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "OrderGroupQuery [siteId=" + siteId + ", category=" + category + ", state=" + state + ", createType="
                + createType + ", owner=" + owner + "]";
    }

}