package com.bsg.dbscale.dao.query;

import java.io.Serializable;

public class ServGroupQuery implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String siteId;
    private String category;
    private Boolean flag;

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

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "ServGroupQuery [siteId=" + siteId + ", category=" + category + ", flag=" + flag + "]";
    }

}
