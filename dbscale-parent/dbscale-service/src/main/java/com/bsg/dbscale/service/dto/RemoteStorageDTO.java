package com.bsg.dbscale.service.dto;

import java.io.Serializable;

public class RemoteStorageDTO extends IdentificationDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String vendor;
    private String version;
    private String ip;
    private Integer port;
    private Long capacity;
    private Long used;
    private DisplayDTO type;
    private Boolean enabled;
    private DisplayDTO state;
    private String description;
    private IdentificationDTO site;
    private InfoDTO created;

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
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

    public Long getCapacity() {
        return capacity;
    }

    public void setCapacity(Long capacity) {
        this.capacity = capacity;
    }

    public Long getUsed() {
        return used;
    }

    public void setUsed(Long used) {
        this.used = used;
    }

    public DisplayDTO getType() {
        return type;
    }

    public void setType(DisplayDTO type) {
        this.type = type;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public DisplayDTO getState() {
        return state;
    }

    public void setState(DisplayDTO state) {
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public IdentificationDTO getSite() {
        return site;
    }

    public void setSite(IdentificationDTO site) {
        this.site = site;
    }

    public InfoDTO getCreated() {
        return created;
    }

    public void setCreated(InfoDTO created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return super.toString() + "RemoteStorageDTO [vendor=" + vendor + ", version=" + version + ", ip=" + ip
                + ", port=" + port + ", capacity=" + capacity + ", used=" + used + ", type=" + type + ", enabled="
                + enabled + ", state=" + state + ", description=" + description + ", site=" + site + ", created="
                + created + "]";
    }

}
