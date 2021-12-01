package com.bsg.dbscale.service.dto;

public class NodeDTO {

    private String id;
    private String name;
    private String ip;
    private StatisticsDTO<Double> cpu;
    private StatisticsDTO<Double> mem;
    private StatisticsDTO<Double> storage;
    private String kernelVersion;
    private String osImage;
    private String containerRuntimeVersion;
    private String kubeletVersion;
    private String kubeProxyVersion;
    private String operatingSystem;
    private DisplayDTO architecture;
    private Boolean enabled;
    private String message;
    private String gmtCreated;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public StatisticsDTO<Double> getCpu() {
        return cpu;
    }

    public void setCpu(StatisticsDTO<Double> cpu) {
        this.cpu = cpu;
    }

    public StatisticsDTO<Double> getMem() {
        return mem;
    }

    public void setMem(StatisticsDTO<Double> mem) {
        this.mem = mem;
    }

    public StatisticsDTO<Double> getStorage() {
        return storage;
    }

    public void setStorage(StatisticsDTO<Double> storage) {
        this.storage = storage;
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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getGmtCreated() {
        return gmtCreated;
    }

    public void setGmtCreated(String gmtCreated) {
        this.gmtCreated = gmtCreated;
    }

    @Override
    public String toString() {
        return "NodeDTO [id=" + id + ", name=" + name + ", ip=" + ip + ", cpu=" + cpu + ", mem=" + mem + ", storage="
                + storage + ", kernelVersion=" + kernelVersion + ", osImage=" + osImage + ", containerRuntimeVersion="
                + containerRuntimeVersion + ", kubeletVersion=" + kubeletVersion + ", kubeProxyVersion="
                + kubeProxyVersion + ", operatingSystem=" + operatingSystem + ", architecture=" + architecture
                + ", enabled=" + enabled + ", message=" + message + ", gmtCreated=" + gmtCreated + "]";
    }

}
