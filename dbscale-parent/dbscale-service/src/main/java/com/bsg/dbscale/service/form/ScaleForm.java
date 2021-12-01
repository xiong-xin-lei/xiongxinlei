package com.bsg.dbscale.service.form;

import java.io.Serializable;

public class ScaleForm implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 类型
     */
    private String type;

    /**
     * CPU数量
     */
    private Double cpuCnt;

    /**
     * 内存容量
     */
    private Double memSize;

    private Boolean enabled;

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

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "ScaleForm [type=" + type + ", cpuCnt=" + cpuCnt + ", memSize=" + memSize + ", enabled=" + enabled + "]";
    }

}