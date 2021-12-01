package com.bsg.dbscale.cm.query;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

public class CmImageQuery implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @JSONField(name = "id")
    private String id;

    @JSONField(name = "site_id")
    private String siteId;

    @JSONField(name = "type")
    private String type;

    @JSONField(name = "major")
    private Integer major;

    @JSONField(name = "minor")
    private Integer minor;

    @JSONField(name = "patch")
    private Integer patch;

    @JSONField(name = "build")
    private Integer build;

    @JSONField(name = "arch")
    private String arch;

    @JSONField(name = "unschedulable")
    private Boolean unschedulable;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getMajor() {
        return major;
    }

    public void setMajor(Integer major) {
        this.major = major;
    }

    public Integer getMinor() {
        return minor;
    }

    public void setMinor(Integer minor) {
        this.minor = minor;
    }

    public Integer getPatch() {
        return patch;
    }

    public void setPatch(Integer patch) {
        this.patch = patch;
    }

    public Integer getBuild() {
        return build;
    }

    public void setBuild(Integer build) {
        this.build = build;
    }

    public String getArch() {
        return arch;
    }

    public void setArch(String arch) {
        this.arch = arch;
    }

    public Boolean getUnschedulable() {
        return unschedulable;
    }

    public void setUnschedulable(Boolean unschedulable) {
        this.unschedulable = unschedulable;
    }

    @Override
    public String toString() {
        return "CmImageQuery [id=" + id + ", siteId=" + siteId + ", type=" + type + ", major=" + major + ", minor="
                + minor + ", patch=" + patch + ", build=" + build + ", arch=" + arch + ", unschedulable="
                + unschedulable + "]";
    }

}
