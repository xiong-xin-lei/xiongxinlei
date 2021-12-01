package com.bsg.dbscale.service.form;

import java.io.Serializable;

public class ServScaleCpuMemForm implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String type;
    private Double cpuCnt;
    private Double memSize;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getCpuCnt() {
        return cpuCnt;
    }

    public void setCpuCnt(Double cpuCnt) {
        this.cpuCnt = cpuCnt;
    }

    public Double getMemSize() {
        return memSize;
    }

    public void setMemSize(Double memSize) {
        this.memSize = memSize;
    }

    @Override
    public String toString() {
        return "ServScaleCpuMemForm [type=" + type + ", cpuCnt=" + cpuCnt + ", memSize=" + memSize + "]";
    }

}
