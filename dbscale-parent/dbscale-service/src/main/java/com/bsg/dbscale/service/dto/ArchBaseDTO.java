package com.bsg.dbscale.service.dto;

import java.io.Serializable;

public class ArchBaseDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String name;
    private DisplayDTO mode;
    private Integer unitCnt;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DisplayDTO getMode() {
        return mode;
    }

    public void setMode(DisplayDTO mode) {
        this.mode = mode;
    }

    public Integer getUnitCnt() {
        return unitCnt;
    }

    public void setUnitCnt(Integer unitCnt) {
        this.unitCnt = unitCnt;
    }

    @Override
    public String toString() {
        return "ArchBaseDTO [name=" + name + ", mode=" + mode + ", unitCnt=" + unitCnt + "]";
    }

}
