package com.bsg.dbscale.service.dto;

import java.io.Serializable;

public class BusinessSubsystemBaseDTO extends IdentificationDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private IdentificationDTO businessSystem;

    public IdentificationDTO getBusinessSystem() {
        return businessSystem;
    }

    public void setBusinessSystem(IdentificationDTO businessSystem) {
        this.businessSystem = businessSystem;
    }

    @Override
    public String toString() {
        return super.toString() + "BusinessSubsystemBaseDTO [businessSystem=" + businessSystem + "]";
    }

}
