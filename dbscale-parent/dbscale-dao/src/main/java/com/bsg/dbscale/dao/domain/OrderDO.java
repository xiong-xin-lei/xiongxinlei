package com.bsg.dbscale.dao.domain;

import java.io.Serializable;

public class OrderDO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String id;
    private String orderGroupId;
    private String type;
    private Integer majorVersion;
    private Integer minorVersion;
    private Integer patchVersion;
    private Integer buildVersion;
    private String archMode;
    private Integer unitCnt;
    private Double cpuCnt;
    private Double memSize;
    private String diskType;
    private Integer dataSize;
    private Integer logSize;
    private Integer port;
    private String cfg;
    private Integer cnt;
    private Boolean clusterHA;
    private Boolean hostHA;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderGroupId() {
        return orderGroupId;
    }

    public void setOrderGroupId(String orderGroupId) {
        this.orderGroupId = orderGroupId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getMajorVersion() {
        return majorVersion;
    }

    public void setMajorVersion(Integer majorVersion) {
        this.majorVersion = majorVersion;
    }

    public Integer getMinorVersion() {
        return minorVersion;
    }

    public void setMinorVersion(Integer minorVersion) {
        this.minorVersion = minorVersion;
    }

    public Integer getPatchVersion() {
        return patchVersion;
    }

    public void setPatchVersion(Integer patchVersion) {
        this.patchVersion = patchVersion;
    }

    public Integer getBuildVersion() {
        return buildVersion;
    }

    public void setBuildVersion(Integer buildVersion) {
        this.buildVersion = buildVersion;
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

    public String getCfg() {
        return cfg;
    }

    public void setCfg(String cfg) {
        this.cfg = cfg;
    }

    public Integer getCnt() {
        return cnt;
    }

    public void setCnt(Integer cnt) {
        this.cnt = cnt;
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
        return "OrderDO [id=" + id + ", orderGroupId=" + orderGroupId + ", type=" + type + ", majorVersion="
                + majorVersion + ", minorVersion=" + minorVersion + ", patchVersion=" + patchVersion + ", buildVersion="
                + buildVersion + ", archMode=" + archMode + ", unitCnt=" + unitCnt + ", cpuCnt=" + cpuCnt + ", memSize="
                + memSize + ", diskType=" + diskType + ", dataSize=" + dataSize + ", logSize=" + logSize + ", port="
                + port + ", cfg=" + cfg + ", cnt=" + cnt + ", clusterHA=" + clusterHA + ", hostHA=" + hostHA + "]";
    }

}
