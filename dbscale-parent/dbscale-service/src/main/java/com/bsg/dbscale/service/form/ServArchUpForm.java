package com.bsg.dbscale.service.form;

import java.io.Serializable;

public class ServArchUpForm implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String type;
    private String archMode;
    private Integer unitCnt;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getArchMode() {
        return archMode;
    }

    public void setArchMode(String archMode) {
        this.archMode = archMode;
    }

    public Integer getUnitCnt() {
        return unitCnt;
    }

    public void setUnitCnt(Integer unitCnt) {
        this.unitCnt = unitCnt;
    }

    @Override
    public String toString() {
        return "ServArchUpForm [type=" + type + ", archMode=" + archMode + ", unitCnt=" + unitCnt + "]";
    }
}
