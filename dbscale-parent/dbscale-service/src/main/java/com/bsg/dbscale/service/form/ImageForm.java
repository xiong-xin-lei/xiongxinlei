package com.bsg.dbscale.service.form;

import java.io.Serializable;

public class ImageForm implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String siteId;
    private String type;
    private String arch;
    private Integer major;
    private Integer minor;
    private Integer patch;
    private Integer build;
    private Boolean enabled;
    private Integer exporterPort;
    private String description;

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getArch() {
        return arch;
    }

    public void setArch(String arch) {
        this.arch = arch;
    }

    public Integer getMajor() {
        return major;
    }

    public void setMajor(Integer major) {
        this.major = major;
    }

    public Integer getMinor() {
        return minor;
    }

    public void setMinor(Integer minor) {
        this.minor = minor;
    }

    public Integer getPatch() {
        return patch;
    }

    public void setPatch(Integer patch) {
        this.patch = patch;
    }

    public Integer getBuild() {
        return build;
    }

    public void setBuild(Integer build) {
        this.build = build;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Integer getExporterPort() {
        return exporterPort;
    }

    public void setExporterPort(Integer exporterPort) {
        this.exporterPort = exporterPort;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "ImageForm [siteId=" + siteId + ", type=" + type + ", arch=" + arch + ", major=" + major + ", minor="
                + minor + ", patch=" + patch + ", build=" + build + ", enabled=" + enabled + ", exporterPort="
                + exporterPort + ", description=" + description + "]";
    }

}
