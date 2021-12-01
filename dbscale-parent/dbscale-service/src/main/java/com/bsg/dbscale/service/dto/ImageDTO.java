package com.bsg.dbscale.service.dto;

import java.io.Serializable;

public class ImageDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String id;
    private DisplayDTO type;
    private Boolean stateful;
    private VersionDTO version;
    private DisplayDTO architecture;
    private Boolean enabled;
    private String description;
    private IdentificationDTO site;
    private String gmtCreated;

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

    public Boolean getStateful() {
        return stateful;
    }

    public void setStateful(Boolean stateful) {
        this.stateful = stateful;
    }

    public VersionDTO getVersion() {
        return version;
    }

    public void setVersion(VersionDTO version) {
        this.version = version;
    }

    public DisplayDTO getArchitecture() {
        return architecture;
    }

    public void setArchitecture(DisplayDTO architecture) {
        this.architecture = architecture;
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

    public IdentificationDTO getSite() {
        return site;
    }

    public void setSite(IdentificationDTO site) {
        this.site = site;
    }

    public String getGmtCreated() {
        return gmtCreated;
    }

    public void setGmtCreated(String gmtCreated) {
        this.gmtCreated = gmtCreated;
    }

    @Override
    public String toString() {
        return "ImageDTO [id=" + id + ", type=" + type + ", stateful=" + stateful + ", version=" + version
                + ", architecture=" + architecture + ", enabled=" + enabled + ", description=" + description + ", site="
                + site + ", gmtCreated=" + gmtCreated + "]";
    }

}
