package com.bsg.dbscale.service.form;

import java.io.Serializable;

public class PVCStorageForm implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String siteId;
    private String name;
    private String provisioner;
    private Boolean enabled;
    private String description;

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
        return "PVCStorageForm [siteId=" + siteId + ", name=" + name + ", provisioner=" + provisioner + ", enabled="
                + enabled + ", description=" + description + "]";
    }

}
