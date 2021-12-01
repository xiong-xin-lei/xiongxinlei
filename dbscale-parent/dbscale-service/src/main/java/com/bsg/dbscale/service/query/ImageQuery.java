package com.bsg.dbscale.service.query;

import java.io.Serializable;

public class ImageQuery implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String siteId;
    private String type;
    private String architecture;
    private Boolean enabled;
    private Integer major;
    private Integer minor;

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

    public String getArchitecture() {
        return architecture;
    }

    public void setArchitecture(String architecture) {
        this.architecture = architecture;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
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

    @Override
    public String toString() {
        return "ImageQuery [siteId=" + siteId + ", type=" + type + ", architecture=" + architecture + ", enabled="
                + enabled + ", major=" + major + ", minor=" + minor + "]";
    }

}
