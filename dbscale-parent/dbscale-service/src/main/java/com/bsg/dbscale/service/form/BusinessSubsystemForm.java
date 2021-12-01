package com.bsg.dbscale.service.form;

import java.io.Serializable;

public class BusinessSubsystemForm implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 业务系统编码
     */
    private String businessSystemId;

    /**
     * 名称
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

    public String getBusinessSystemId() {
        return businessSystemId;
    }

    public void setBusinessSystemId(String businessSystemId) {
        this.businessSystemId = businessSystemId;
    }

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

    @Override
    public String toString() {
        return "BusinessSubsystemForm [businessSystemId=" + businessSystemId + ", name=" + name + ", enabled=" + enabled
                + ", description=" + description + "]";
    }

}
