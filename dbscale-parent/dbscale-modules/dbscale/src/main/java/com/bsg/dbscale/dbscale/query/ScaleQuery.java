package com.bsg.dbscale.dbscale.query;

import java.io.Serializable;

public class ScaleQuery implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String type;
    private Boolean enabled;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "ScaleQuery [type=" + type + ", enabled=" + enabled + "]";
    }

}
