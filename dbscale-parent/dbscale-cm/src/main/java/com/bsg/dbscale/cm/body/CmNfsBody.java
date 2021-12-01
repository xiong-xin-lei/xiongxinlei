package com.bsg.dbscale.cm.body;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

public class CmNfsBody implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

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

    @JSONField(name = "site_id")
    private String siteId;

    @JSONField(name = "zone")
    private String zone;

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

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    @Override
    public String toString() {
        return "CmNfsBody [name=" + name + ", nfsIp=" + nfsIp + ", nfsSource=" + nfsSource + ", nfsOpts=" + nfsOpts
                + ", desc=" + desc + ", unschedulable=" + unschedulable + ", siteId=" + siteId + ", zone=" + zone + "]";
    }

}
