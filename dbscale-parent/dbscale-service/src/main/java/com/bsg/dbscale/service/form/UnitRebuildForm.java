package com.bsg.dbscale.service.form;

import java.io.Serializable;

public class UnitRebuildForm extends UnitRebuildInitForm implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String hostRelateId;
    private Boolean force;

    public String getHostRelateId() {
        return hostRelateId;
    }

    public void setHostRelateId(String hostRelateId) {
        this.hostRelateId = hostRelateId;
    }

    public Boolean getForce() {
        return force;
    }

    public void setForce(Boolean force) {
        this.force = force;
    }

    @Override
    public String toString() {
        return super.toString() + "UnitRebuildForm [hostRelateId=" + hostRelateId + ", force=" + force + "]";
    }

}
