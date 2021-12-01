package com.bsg.dbscale.service.dto;

import java.io.Serializable;

public class ArchDTO extends ArchBaseDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private DisplayDTO type;

    public DisplayDTO getType() {
        return type;
    }

    public void setType(DisplayDTO type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return super.toString() + "ArchDTO [type=" + type + "]";
    }

}
