package com.bsg.dbscale.service.dto;

import java.io.Serializable;

public class OrderCfgDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String category;
    private DisplayDTO type;
    private ScaleBaseDTO scale;
    private DisplayDTO diskType;
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

    public DisplayDTO getType() {
        return type;
    }

    public void setType(DisplayDTO type) {
        this.type = type;
    }

    public ScaleBaseDTO getScale() {
        return scale;
    }

    public void setScale(ScaleBaseDTO scale) {
        this.scale = scale;
    }

    public DisplayDTO getDiskType() {
        return diskType;
    }

    public void setDiskType(DisplayDTO diskType) {
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
        return "OrderCfgDTO [category=" + category + ", type=" + type + ", scale=" + scale + ", diskType=" + diskType
                + ", dataSize=" + dataSize + ", logSize=" + logSize + ", port=" + port + ", clusterHA=" + clusterHA
                + ", hostHA=" + hostHA + "]";
    }

}
