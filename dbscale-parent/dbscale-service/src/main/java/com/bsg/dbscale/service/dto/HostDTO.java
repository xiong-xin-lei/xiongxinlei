package com.bsg.dbscale.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HostDTO extends IdentificationDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String ip;
    private String room;
    private String seat;
    private Statistics unit;
    private Statistics pod;
    private HostStatisticsDTO<Double> cpu;
    private HostStatisticsDTO<Double> mem;
    private Integer maxUsage;
    private Storage hdd;
    private Storage ssd;
    private IdentificationDTO remoteStorage;
    private Boolean inSuccess;
    private Boolean enabled;
    private DisplayDTO role;
    private String description;
    private String relateId;
    private TaskBaseDTO task;
    private IdentificationDTO site;
    private IdentificationDTO businessArea;
    private IdentificationDTO cluster;
    private InfoDTO created;
    private InfoDTO input;
    private String kernelVersion;
    private String osImage;
    private String operatingSystem;
    private DisplayDTO architecture;
    private String containerRuntimeVersion;
    private String kubeletVersion;
    private String kubeProxyVersion;
    private DisplayDTO state;
    private DisplayDTO nodeState;
    private DisplayDTO agentState;
    private boolean distributable;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getSeat() {
        return seat;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }

    public Statistics getUnit() {
        return unit;
    }

    public void setUnit(Statistics unit) {
        this.unit = unit;
    }

    public Statistics getPod() {
        return pod;
    }

    public void setPod(Statistics pod) {
        this.pod = pod;
    }

    public HostStatisticsDTO<Double> getCpu() {
        return cpu;
    }

    public void setCpu(HostStatisticsDTO<Double> cpu) {
        this.cpu = cpu;
    }

    public HostStatisticsDTO<Double> getMem() {
        return mem;
    }

    public void setMem(HostStatisticsDTO<Double> mem) {
        this.mem = mem;
    }

    public Integer getMaxUsage() {
        return maxUsage;
    }

    public void setMaxUsage(Integer maxUsage) {
        this.maxUsage = maxUsage;
    }

    public Storage getHdd() {
        return hdd;
    }

    public void setHdd(Storage hdd) {
        this.hdd = hdd;
    }

    public Storage getSsd() {
        return ssd;
    }

    public void setSsd(Storage ssd) {
        this.ssd = ssd;
    }

    public IdentificationDTO getRemoteStorage() {
        return remoteStorage;
    }

    public void setRemoteStorage(IdentificationDTO remoteStorage) {
        this.remoteStorage = remoteStorage;
    }

    public Boolean getInSuccess() {
        return inSuccess;
    }

    public void setInSuccess(Boolean inSuccess) {
        this.inSuccess = inSuccess;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public DisplayDTO getRole() {
        return role;
    }

    public void setRole(DisplayDTO role) {
        this.role = role;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRelateId() {
        return relateId;
    }

    public void setRelateId(String relateId) {
        this.relateId = relateId;
    }

    public TaskBaseDTO getTask() {
        return task;
    }

    public void setTask(TaskBaseDTO task) {
        this.task = task;
    }

    public IdentificationDTO getSite() {
        return site;
    }

    public void setSite(IdentificationDTO site) {
        this.site = site;
    }

    public IdentificationDTO getBusinessArea() {
        return businessArea;
    }

    public void setBusinessArea(IdentificationDTO businessArea) {
        this.businessArea = businessArea;
    }

    public IdentificationDTO getCluster() {
        return cluster;
    }

    public void setCluster(IdentificationDTO cluster) {
        this.cluster = cluster;
    }

    public InfoDTO getCreated() {
        return created;
    }

    public void setCreated(InfoDTO created) {
        this.created = created;
    }

    public InfoDTO getInput() {
        return input;
    }

    public void setInput(InfoDTO input) {
        this.input = input;
    }

    public String getKernelVersion() {
        return kernelVersion;
    }

    public void setKernelVersion(String kernelVersion) {
        this.kernelVersion = kernelVersion;
    }

    public String getOsImage() {
        return osImage;
    }

    public void setOsImage(String osImage) {
        this.osImage = osImage;
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(String operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    public DisplayDTO getArchitecture() {
        return architecture;
    }

    public void setArchitecture(DisplayDTO architecture) {
        this.architecture = architecture;
    }

    public String getContainerRuntimeVersion() {
        return containerRuntimeVersion;
    }

    public void setContainerRuntimeVersion(String containerRuntimeVersion) {
        this.containerRuntimeVersion = containerRuntimeVersion;
    }

    public String getKubeletVersion() {
        return kubeletVersion;
    }

    public void setKubeletVersion(String kubeletVersion) {
        this.kubeletVersion = kubeletVersion;
    }

    public String getKubeProxyVersion() {
        return kubeProxyVersion;
    }

    public void setKubeProxyVersion(String kubeProxyVersion) {
        this.kubeProxyVersion = kubeProxyVersion;
    }

    public DisplayDTO getState() {
        return state;
    }

    public void setState(DisplayDTO state) {
        this.state = state;
    }

    public DisplayDTO getNodeState() {
        return nodeState;
    }

    public void setNodeState(DisplayDTO nodeState) {
        this.nodeState = nodeState;
    }

    public DisplayDTO getAgentState() {
        return agentState;
    }

    public void setAgentState(DisplayDTO agentState) {
        this.agentState = agentState;
    }

    public boolean isDistributable() {
        return distributable;
    }

    public void setDistributable(boolean distributable) {
        this.distributable = distributable;
    }

    @Override
    public String toString() {
        return "HostDTO [ip=" + ip + ", room=" + room + ", seat=" + seat + ", unit=" + unit + ", pod=" + pod + ", cpu="
                + cpu + ", mem=" + mem + ", maxUsage=" + maxUsage + ", hdd=" + hdd + ", ssd=" + ssd + ", remoteStorage="
                + remoteStorage + ", inSuccess=" + inSuccess + ", enabled=" + enabled + ", role=" + role
                + ", description=" + description + ", relateId=" + relateId + ", task=" + task + ", site=" + site
                + ", businessArea=" + businessArea + ", cluster=" + cluster + ", created=" + created + ", input="
                + input + ", kernelVersion=" + kernelVersion + ", osImage=" + osImage + ", operatingSystem="
                + operatingSystem + ", architecture=" + architecture + ", containerRuntimeVersion="
                + containerRuntimeVersion + ", kubeletVersion=" + kubeletVersion + ", kubeProxyVersion="
                + kubeProxyVersion + ", state=" + state + ", nodeState=" + nodeState + ", agentState=" + agentState
                + ", distributable=" + distributable + "]";
    }

    public class Storage extends HostStatisticsDTO<Double> {
        private List<String> paths;

        public Storage() {
            paths = new ArrayList<>();
        }

        public List<String> getPaths() {
            return paths;
        }

        public void setPaths(List<String> paths) {
            this.paths = paths;
        }

        @Override
        public String toString() {
            return "Storage [paths=" + paths + "]";
        }

    }

    public class Statistics {
        private Integer cnt;
        private Integer maxCnt;
        private boolean upLimit;

        public Integer getCnt() {
            return cnt;
        }

        public void setCnt(Integer cnt) {
            this.cnt = cnt;
        }

        public Integer getMaxCnt() {
            return maxCnt;
        }

        public void setMaxCnt(Integer maxCnt) {
            this.maxCnt = maxCnt;
        }

        public Boolean getUpLimit() {
            return upLimit;
        }

        public void setUpLimit(Boolean upLimit) {
            this.upLimit = upLimit;
        }

        @Override
        public String toString() {
            return "Statistics [cnt=" + cnt + ", maxCnt=" + maxCnt + ", upLimit=" + upLimit + "]";
        }

    }
}
