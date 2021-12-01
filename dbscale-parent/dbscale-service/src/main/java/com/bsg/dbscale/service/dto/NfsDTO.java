package com.bsg.dbscale.service.dto;

import java.io.Serializable;

public class NfsDTO extends NfsBaseDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private IdentificationDTO site;
    private IdentificationDTO businessArea;
    private InfoDTO created;

    public IdentificationDTO getSite() {
        return site;
    }

    public void setSite(IdentificationDTO site) {
        this.site = site;
    }

    public IdentificationDTO getBusinessArea() {
        return businessArea;
    }

    public void setBusinessArea(IdentificationDTO businessArea) {
        this.businessArea = businessArea;
    }

    public InfoDTO getCreated() {
        return created;
    }

    public void setCreated(InfoDTO created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return super.toString() + "NfsDTO [site=" + site + ", businessArea=" + businessArea + ", created=" + created
                + "]";
    }

}
