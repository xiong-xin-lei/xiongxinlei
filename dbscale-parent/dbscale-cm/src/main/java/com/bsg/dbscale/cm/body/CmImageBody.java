package com.bsg.dbscale.cm.body;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

public class CmImageBody implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @JSONField(name = "site_id")
    private String siteId;

    @JSONField(name = "type")
    private String type;

    @JSONField(name = "arch")
    private String arch;

    @JSONField(name = "major")
    private Integer major;

    @JSONField(name = "minor")
    private Integer minor;

    @JSONField(name = "patch")
    private Integer patch;

    @JSONField(name = "build")
    private Integer build;

    @JSONField(name = "desc")
    private String desc;

    @JSONField(name = "unschedulable")
    private Boolean unschedulable;

    @JSONField(name = "exporter_port")
    private Integer exporterPort;

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

    public String getArch() {
        return arch;
    }

    public void setArch(String arch) {
        this.arch = arch;
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

    public Integer getExporterPort() {
        return exporterPort;
    }

    public void setExporterPort(Integer exporterPort) {
        this.exporterPort = exporterPort;
    }

    @Override
    public String toString() {
        return "CmImageBody [siteId=" + siteId + ", type=" + type + ", arch=" + arch + ", major=" + major + ", minor="
                + minor + ", patch=" + patch + ", build=" + build + ", desc=" + desc + ", unschedulable="
                + unschedulable + ", exporterPort=" + exporterPort + "]";
    }

}
