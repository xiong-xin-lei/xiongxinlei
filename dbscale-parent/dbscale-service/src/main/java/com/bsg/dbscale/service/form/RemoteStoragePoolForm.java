package com.bsg.dbscale.service.form;

import java.io.Serializable;

public class RemoteStoragePoolForm implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String name;
    private String performance;
    private Boolean enabled;
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPerformance() {
        return performance;
    }

    public void setPerformance(String performance) {
        this.performance = performance;
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

    @Override
    public String toString() {
        return "RemoteStoragePoolForm [name=" + name + ", performance=" + performance + ", enabled=" + enabled
                + ", description=" + description + "]";
    }

}
