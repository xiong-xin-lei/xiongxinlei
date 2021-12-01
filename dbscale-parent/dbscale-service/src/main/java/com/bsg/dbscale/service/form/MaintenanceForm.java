package com.bsg.dbscale.service.form;

import java.io.Serializable;

public class MaintenanceForm implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Boolean maintenance;

    public Boolean getMaintenance() {
        return maintenance;
    }

    public void setMaintenance(Boolean maintenance) {
        this.maintenance = maintenance;
    }

    @Override
    public String toString() {
        return "MaintenanceForm [maintenance=" + maintenance + "]";
    }

}
