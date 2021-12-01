package com.bsg.dbscale.service.dto;

import java.io.Serializable;

public class BusinessAreaDTO extends IdentificationDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Boolean enabled;
    private String description;
    private IdentificationDTO site;
    private NfsBaseDTO nfs;
    private InfoDTO created;

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

    public NfsBaseDTO getNfs() {
        return nfs;
    }

    public void setNfs(NfsBaseDTO nfs) {
        this.nfs = nfs;
    }

    public InfoDTO getCreated() {
        return created;
    }

    public void setCreated(InfoDTO created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return super.toString() + "BusinessAreaDTO [enabled=" + enabled + ", description=" + description + ", site="
                + site + ", nfs=" + nfs + ", created=" + created + "]";
    }

}
