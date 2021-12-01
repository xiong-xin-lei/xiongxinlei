package com.bsg.dbscale.dao.domain;

import java.io.Serializable;

public class ScaleDO implements Serializable {

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
     * 内存容量(G)
     */
    private Double memSize;

    /**
     * 名称
     */
    private String name;

    /**
     * 是否可用
     */
    private Boolean enabled;

    /**
     * 显示顺序
     */
    private Long sequence;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    @Override
    public String toString() {
        return "ScaleDO [type=" + type + ", cpuCnt=" + cpuCnt + ", memSize=" + memSize + ", name=" + name + ", enabled="
                + enabled + ", sequence=" + sequence + "]";
    }

}
