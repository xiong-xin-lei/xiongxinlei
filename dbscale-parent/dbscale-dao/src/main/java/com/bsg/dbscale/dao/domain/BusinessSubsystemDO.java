package com.bsg.dbscale.dao.domain;

import java.io.Serializable;
import java.util.Date;

public class BusinessSubsystemDO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private String id;

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

    /**
     * 业务系统编码
     */
    private String businessSystemId;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 所属业务系统
     */
    private BusinessSystemDO businessSystem;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getBusinessSystemId() {
        return businessSystemId;
    }

    public void setBusinessSystemId(String businessSystemId) {
        this.businessSystemId = businessSystemId;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public BusinessSystemDO getBusinessSystem() {
        return businessSystem;
    }

    public void setBusinessSystem(BusinessSystemDO businessSystem) {
        this.businessSystem = businessSystem;
    }

    @Override
    public String toString() {
        return "BusinessSubsystemDO [id=" + id + ", name=" + name + ", enabled=" + enabled + ", description="
                + description + ", businessSystemId=" + businessSystemId + ", gmtCreate=" + gmtCreate
                + ", businessSystem=" + businessSystem + "]";
    }

}
