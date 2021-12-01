package com.bsg.dbscale.service.dto;

import java.io.Serializable;

public class CmhaUnitDTO extends UnitBaseDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private DisplayDTO role;

    public DisplayDTO getRole() {
        return role;
    }

    public void setRole(DisplayDTO role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return super.toString() + "CmhaUnitDTO [role=" + role + "]";
    }

}
