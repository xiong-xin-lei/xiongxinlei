package com.bsg.dbscale.service.form;

import java.io.Serializable;

public class NfsForm implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String name;
    private String businessAreaId;
    private String nfsIp;
    private String nfsSource;
    private String nfsOpts;
    private Boolean enabled;
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBusinessAreaId() {
        return businessAreaId;
    }

    public void setBusinessAreaId(String businessAreaId) {
        this.businessAreaId = businessAreaId;
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
        return "NfsForm [name=" + name + ", businessAreaId=" + businessAreaId + ", nfsIp=" + nfsIp + ", nfsSource="
                + nfsSource + ", nfsOpts=" + nfsOpts + ", enabled=" + enabled + ", description=" + description + "]";
    }

}
