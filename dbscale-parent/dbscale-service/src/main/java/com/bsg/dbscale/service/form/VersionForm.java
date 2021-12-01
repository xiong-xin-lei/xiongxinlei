package com.bsg.dbscale.service.form;

import java.io.Serializable;

public class VersionForm implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Integer major;
    private Integer minor;
    private Integer patch;
    private Integer build;

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

    @Override
    public String toString() {
        return "VersionForm [major=" + major + ", minor=" + minor + ", patch=" + patch + ", build=" + build + "]";
    }

}
