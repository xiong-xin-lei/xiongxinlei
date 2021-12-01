package com.bsg.dbscale.service.form;

import java.io.Serializable;

public class BusinessAreaForm implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 组名
     */
    private String name;

    /**
     * 是否可用
     */
    private Boolean enabled;

    /**
     * 描述
     */
    private String description;

    /**
     * 所属站点
     */
    private String siteId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    @Override
    public String toString() {
        return "BusinessAreaForm [name=" + name + ", enabled=" + enabled + ", description=" + description + ", siteId="
                + siteId + "]";
    }

}
