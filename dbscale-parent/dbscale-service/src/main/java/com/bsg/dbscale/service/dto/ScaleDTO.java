package com.bsg.dbscale.service.dto;

import java.io.Serializable;

public class ScaleDTO extends ScaleBaseDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private DisplayDTO type;
    private Boolean enabled;

    public DisplayDTO getType() {
        return type;
    }

    public void setType(DisplayDTO type) {
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
        return super.toString() + "ScaleDTO [type=" + type + ", enabled=" + enabled + "]";
    }

}
