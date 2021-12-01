package com.bsg.dbscale.cm.body;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

public class CmStorageclassBody implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @JSONField(name = "site_id")
    private String siteId;

    @JSONField(name = "name")
    private String name;

    @JSONField(name = "provisioner")
    private String provisioner;

    @JSONField(name = "desc")
    private String desc;

    @JSONField(name = "unschedulable")
    private Boolean unschedulable;

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProvisioner() {
        return provisioner;
    }

    public void setProvisioner(String provisioner) {
        this.provisioner = provisioner;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Boolean getUnschedulable() {
        return unschedulable;
    }

    public void setUnschedulable(Boolean unschedulable) {
        this.unschedulable = unschedulable;
    }

    @Override
    public String toString() {
        return "CmStorageclassBody [siteId=" + siteId + ", name=" + name + ", provisioner=" + provisioner + ", desc="
                + desc + ", unschedulable=" + unschedulable + "]";
    }

}
