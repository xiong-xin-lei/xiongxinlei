package com.bsg.dbscale.cm.model;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

public class CmNfs implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @JSONField(name = "id")
    private String id;

    @JSONField(name = "name")
    private String name;

    @JSONField(name = "nfs_ip")
    private String nfsIp;

    @JSONField(name = "nfs_source")
    private String nfsSource;

    @JSONField(name = "nfs_opts")
    private String nfsOpts;

    @JSONField(name = "desc")
    private String desc;

    @JSONField(name = "unschedulable")
    private Boolean unschedulable;

    @JSONField(name = "status")
    private String status;

    @JSONField(name = "zone")
    private String zone;

    @JSONField(name = "site")
    private CmSiteBase site;

    @JSONField(name = "created_at")
    private String createdAt;

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

    public String getNfsIp() {
        return nfsIp;
    }

    public void setNfsIp(String nfsIp) {
        this.nfsIp = nfsIp;
    }

    public String getNfsSource() {
        return nfsSource;
    }

    public void setNfsSource(String nfsSource) {
        this.nfsSource = nfsSource;
    }

    public String getNfsOpts() {
        return nfsOpts;
    }

    public void setNfsOpts(String nfsOpts) {
        this.nfsOpts = nfsOpts;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
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
        return "CmNfs [id=" + id + ", name=" + name + ", nfsIp=" + nfsIp + ", nfsSource=" + nfsSource + ", nfsOpts="
                + nfsOpts + ", desc=" + desc + ", unschedulable=" + unschedulable + ", status=" + status + ", zone="
                + zone + ", site=" + site + ", createdAt=" + createdAt + "]";
    }

}
