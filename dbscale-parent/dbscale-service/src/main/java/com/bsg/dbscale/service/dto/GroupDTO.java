package com.bsg.dbscale.service.dto;

import java.io.Serializable;

public class GroupDTO extends IdentificationDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String description;
    private Boolean sys;
    private InfoDTO created;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getSys() {
        return sys;
    }

    public void setSys(Boolean sys) {
        this.sys = sys;
    }

    public InfoDTO getCreated() {
        return created;
    }

    public void setCreated(InfoDTO created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return super.toString() + "GroupDTO [description=" + description + ", sys=" + sys + ", created=" + created
                + "]";
    }

}
