package com.bsg.dbscale.service.dto;

import java.io.Serializable;

public class PVCStorageDTO extends IdentificationDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String provisioner;
    private DisplayDTO reclaimPolicy;
    private DisplayDTO volumeBingdingType;
    private Boolean allowVolumeExpansion;
    private Boolean enabled;
    private String description;
    private IdentificationDTO site;
    private InfoDTO created;

    public String getProvisioner() {
        return provisioner;
    }

    public void setProvisioner(String provisioner) {
        this.provisioner = provisioner;
    }

    public DisplayDTO getReclaimPolicy() {
        return reclaimPolicy;
    }

    public void setReclaimPolicy(DisplayDTO reclaimPolicy) {
        this.reclaimPolicy = reclaimPolicy;
    }

    public DisplayDTO getVolumeBingdingType() {
        return volumeBingdingType;
    }

    public void setVolumeBingdingType(DisplayDTO volumeBingdingType) {
        this.volumeBingdingType = volumeBingdingType;
    }

    public Boolean getAllowVolumeExpansion() {
        return allowVolumeExpansion;
    }

    public void setAllowVolumeExpansion(Boolean allowVolumeExpansion) {
        this.allowVolumeExpansion = allowVolumeExpansion;
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

    public IdentificationDTO getSite() {
        return site;
    }

    public void setSite(IdentificationDTO site) {
        this.site = site;
    }

    public InfoDTO getCreated() {
        return created;
    }

    public void setCreated(InfoDTO created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return super.toString() + "PVCStorageDTO [provisioner=" + provisioner + ", reclaimPolicy=" + reclaimPolicy
                + ", volumeBingdingType=" + volumeBingdingType + ", allowVolumeExpansion=" + allowVolumeExpansion
                + ", enabled=" + enabled + ", description=" + description + ", site=" + site + ", created=" + created
                + "]";
    }

}
