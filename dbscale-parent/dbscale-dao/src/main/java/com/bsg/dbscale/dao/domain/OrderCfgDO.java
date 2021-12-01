package com.bsg.dbscale.dao.domain;

import java.io.Serializable;

public class OrderCfgDO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String category;
    private String type;
    private Double cpuCnt;
    private Double memSize;
    private String diskType;
    private Integer dataSize;
    private Integer logSize;
    private Integer port;
    private Boolean clusterHA;
    private Boolean hostHA;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

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

    public String getDiskType() {
        return diskType;
    }

    public void setDiskType(String diskType) {
        this.diskType = diskType;
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

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Boolean getClusterHA() {
        return clusterHA;
    }

    public void setClusterHA(Boolean clusterHA) {
        this.clusterHA = clusterHA;
    }

    public Boolean getHostHA() {
        return hostHA;
    }

    public void setHostHA(Boolean hostHA) {
        this.hostHA = hostHA;
    }

    @Override
    public String toString() {
        return "OrderCfgDO [category=" + category + ", type=" + type + ", cpuCnt=" + cpuCnt + ", memSize=" + memSize
                + ", diskType=" + diskType + ", dataSize=" + dataSize + ", logSize=" + logSize + ", port=" + port
                + ", clusterHA=" + clusterHA + ", hostHA=" + hostHA + "]";
    }

}
