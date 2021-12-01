package com.bsg.dbscale.cm.model;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

public class CmImage extends CmImageBase implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @JSONField(name = "arch")
    private String arch;

    @JSONField(name = "desc")
    private String desc;

    @JSONField(name = "unschedulable")
    private Boolean unschedulable;

    @JSONField(name = "exporter_port")
    private Integer exporterPort;

    @JSONField(name = "site")
    private CmSiteBase site;

    @JSONField(name = "created_at")
    private String createdAt;

    public String getArch() {
        return arch;
    }

    public void setArch(String arch) {
        this.arch = arch;
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

    public CmSiteBase getSite() {
        return site;
    }

    public void setSite(CmSiteBase site) {
        this.site = site;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return super.toString() + "CmImage [arch=" + arch + ", desc=" + desc + ", unschedulable=" + unschedulable
                + ", exporterPort=" + exporterPort + ", site=" + site + ", createdAt=" + createdAt + "]";
    }

}
