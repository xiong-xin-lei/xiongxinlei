package com.bsg.dbscale.dao.query;

import java.io.Serializable;

public class BusinessSystemQuery implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String owner;
    private Boolean enabled;

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "BusinessSystemQuery [owner=" + owner + ", enabled=" + enabled + "]";
    }

}
