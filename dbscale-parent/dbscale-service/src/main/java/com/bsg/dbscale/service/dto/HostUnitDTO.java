package com.bsg.dbscale.service.dto;

import java.io.Serializable;

public class HostUnitDTO extends UnitBaseDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private ServGroupBaseDTO servGroup;

    public ServGroupBaseDTO getServGroup() {
        return servGroup;
    }

    public void setServGroup(ServGroupBaseDTO servGroup) {
        this.servGroup = servGroup;
    }

    @Override
    public String toString() {
        return "HostUnitDTO [servGroup=" + servGroup + "]";
    }

}
