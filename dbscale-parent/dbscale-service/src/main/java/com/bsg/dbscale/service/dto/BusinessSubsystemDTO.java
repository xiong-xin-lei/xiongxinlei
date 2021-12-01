package com.bsg.dbscale.service.dto;

import java.io.Serializable;

public class BusinessSubsystemDTO extends BusinessSubsystemBaseDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Boolean enabled;
    private String description;
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

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    @Override
    public String toString() {
        return super.toString() + "BusinessSubsystemDTO [enabled=" + enabled + ", description=" + description
                + ", gmtCreate=" + gmtCreate + "]";
    }

}
