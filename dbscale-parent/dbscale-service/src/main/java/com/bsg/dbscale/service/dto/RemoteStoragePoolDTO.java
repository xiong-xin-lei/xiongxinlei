package com.bsg.dbscale.service.dto;

import java.io.Serializable;

public class RemoteStoragePoolDTO extends IdentificationDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private DisplayDTO performance;
    private Long capacity;
    private Long used;
    private IdentificationDTO remoteStorage;
    private Boolean enabled;
    private String description;
    private InfoDTO created;

    public DisplayDTO getPerformance() {
        return performance;
    }

    public void setPerformance(DisplayDTO performance) {
        this.performance = performance;
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

    public IdentificationDTO getRemoteStorage() {
        return remoteStorage;
    }

    public void setRemoteStorage(IdentificationDTO remoteStorage) {
        this.remoteStorage = remoteStorage;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public InfoDTO getCreated() {
        return created;
    }

    public void setCreated(InfoDTO created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return "RemoteStoragePoolDTO [performance=" + performance + ", capacity=" + capacity + ", used=" + used
                + ", remoteStorage=" + remoteStorage + ", enabled=" + enabled + ", description=" + description
                + ", created=" + created + "]";
    }

}
