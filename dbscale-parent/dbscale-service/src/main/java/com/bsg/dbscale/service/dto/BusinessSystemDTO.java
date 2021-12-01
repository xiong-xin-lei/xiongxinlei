package com.bsg.dbscale.service.dto;

import java.io.Serializable;

public class BusinessSystemDTO extends IdentificationDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Boolean enabled;
    private String description;
    private String owner;
    private String ownerName;
    private String gmtCreate;

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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    @Override
    public String toString() {
        return super.toString() + "BusinessSystemDTO [enabled=" + enabled + ", description=" + description + ", owner="
                + owner + ", ownerName=" + ownerName + ", gmtCreate=" + gmtCreate + "]";
    }

}
