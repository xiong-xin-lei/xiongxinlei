package com.bsg.dbscale.service.dto;

import java.io.Serializable;

public class UnitBaseDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String id;
    private DisplayDTO type;
    private String relateId;
    private Host host;
    private String ip;
    private Integer port;
    private VersionDTO version;
    private Double cpuCnt;
    private Double memSize;
    private DisplayDTO diskType;
    private Integer dataSize;
    private Integer logSize;
    private DisplayDTO state;
    private DisplayDTO podState;
    private TaskBaseDTO task;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DisplayDTO getType() {
        return type;
    }

    public void setType(DisplayDTO type) {
        this.type = type;
    }

    public String getRelateId() {
        return relateId;
    }

    public void setRelateId(String relateId) {
        this.relateId = relateId;
    }

    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public VersionDTO getVersion() {
        return version;
    }

    public void setVersion(VersionDTO version) {
        this.version = version;
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

    public DisplayDTO getState() {
        return state;
    }

    public void setState(DisplayDTO state) {
        this.state = state;
    }

    public DisplayDTO getPodState() {
        return podState;
    }

    public void setPodState(DisplayDTO podState) {
        this.podState = podState;
    }

    public TaskBaseDTO getTask() {
        return task;
    }

    public void setTask(TaskBaseDTO task) {
        this.task = task;
    }

    @Override
    public String toString() {
        return "UnitDTO [id=" + id + ", type=" + type + ", relateId=" + relateId + ", host=" + host + ", ip=" + ip
                + ", port=" + port + ", version=" + version + ", cpuCnt=" + cpuCnt + ", memSize=" + memSize
                + ", diskType=" + diskType + ", dataSize=" + dataSize + ", logSize=" + logSize + ", state=" + state
                + ", podState=" + podState + ", task=" + task + "]";
    }

    public class Host {
        private String ip;
        private String name;
        private IdentificationDTO cluster;

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public IdentificationDTO getCluster() {
            return cluster;
        }

        public void setCluster(IdentificationDTO cluster) {
            this.cluster = cluster;
        }

        @Override
        public String toString() {
            return "Host [ip=" + ip + ", name=" + name + ", cluster=" + cluster + "]";
        }

    }

    public class HostState extends Host {
        private DisplayDTO state;

        public DisplayDTO getState() {
            return state;
        }

        public void setState(DisplayDTO state) {
            this.state = state;
        }

        @Override
        public String toString() {
            return super.toString() + "HostState [state=" + state + "]";
        }

    }
}
