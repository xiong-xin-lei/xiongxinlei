package com.bsg.dbscale.service.dto;

import java.io.Serializable;

public class NfsBaseDTO extends IdentificationDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String nfsIp;
    private String nfsSource;
    private String nfsOpts;
    private Boolean enabled;
    private String description;
    private DisplayDTO state;

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

    public DisplayDTO getState() {
        return state;
    }

    public void setState(DisplayDTO state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return super.toString() + "NfsBaseDTO [nfsIp=" + nfsIp + ", nfsSource=" + nfsSource + ", nfsOpts=" + nfsOpts
                + ", enabled=" + enabled + ", description=" + description + ", state=" + state + "]";
    }

}
