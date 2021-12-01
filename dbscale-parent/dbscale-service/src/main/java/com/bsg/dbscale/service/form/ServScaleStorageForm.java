package com.bsg.dbscale.service.form;

import java.io.Serializable;

public class ServScaleStorageForm implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String type;
    private Integer dataSize;
    private Integer logSize;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getDataSize() {
        return dataSize;
    }

    public void setDataSize(Integer dataSize) {
        this.dataSize = dataSize;
    }

    public Integer getLogSize() {
        return logSize;
    }

    public void setLogSize(Integer logSize) {
        this.logSize = logSize;
    }

    @Override
    public String toString() {
        return "ServScaleStorageForm [type=" + type + ", dataSize=" + dataSize + ", logSize=" + logSize + "]";
    }

}
