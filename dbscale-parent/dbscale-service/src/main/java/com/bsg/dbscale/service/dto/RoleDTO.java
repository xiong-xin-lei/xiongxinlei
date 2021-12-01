package com.bsg.dbscale.service.dto;

import java.io.Serializable;

public class RoleDTO extends IdentificationDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String description;
    private Boolean manager;
    private DisplayDTO dataScope;
    private Boolean sys;
    private InfoDTO created;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getManager() {
        return manager;
    }

    public void setManager(Boolean manager) {
        this.manager = manager;
    }

    public DisplayDTO getDataScope() {
        return dataScope;
    }

    public void setDataScope(DisplayDTO dataScope) {
        this.dataScope = dataScope;
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
        return "RoleDTO [description=" + description + ", manager=" + manager + ", dataScope=" + dataScope + ", sys="
                + sys + ", created=" + created + "]";
    }

}
