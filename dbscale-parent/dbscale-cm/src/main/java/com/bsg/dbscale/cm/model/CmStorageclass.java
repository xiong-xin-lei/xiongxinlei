package com.bsg.dbscale.cm.model;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

public class CmStorageclass extends CmRemoteStorageBase implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @JSONField(name = "provisioner")
    private String provisioner;

    @JSONField(name = "reclaim_policy")
    private String reclaimPolicy;

    @JSONField(name = "volume_binding_mode")
    private String volumeBingdingMode;

    @JSONField(name = "allow_volume_expansion")
    private Boolean allowVolumeExpansion;

    @JSONField(name = "unschedulable")
    private Boolean unschedulable;

    @JSONField(name = "state")
    private String state;

    @JSONField(name = "desc")
    private String desc;

    @JSONField(name = "site")
    private CmSiteBase site;

    @JSONField(name = "created_at")
    private String createdAt;

    public String getProvisioner() {
        return provisioner;
    }

    public void setProvisioner(String provisioner) {
        this.provisioner = provisioner;
    }

    public String getReclaimPolicy() {
        return reclaimPolicy;
    }

    public void setReclaimPolicy(String reclaimPolicy) {
        this.reclaimPolicy = reclaimPolicy;
    }

    public String getVolumeBingdingMode() {
        return volumeBingdingMode;
    }

    public void setVolumeBingdingMode(String volumeBingdingMode) {
        this.volumeBingdingMode = volumeBingdingMode;
    }

    public Boolean getAllowVolumeExpansion() {
        return allowVolumeExpansion;
    }

    public void setAllowVolumeExpansion(Boolean allowVolumeExpansion) {
        this.allowVolumeExpansion = allowVolumeExpansion;
    }

    public Boolean getUnschedulable() {
        return unschedulable;
    }

    public void setUnschedulable(Boolean unschedulable) {
        this.unschedulable = unschedulable;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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
        return "CmStorageclass [provisioner=" + provisioner + ", reclaimPolicy=" + reclaimPolicy
                + ", volumeBingdingMode=" + volumeBingdingMode + ", allowVolumeExpansion=" + allowVolumeExpansion
                + ", unschedulable=" + unschedulable + ", state=" + state + ", desc=" + desc + ", site=" + site
                + ", createdAt=" + createdAt + "]";
    }

}
