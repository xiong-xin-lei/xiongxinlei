package com.bsg.dbscale.cm.model;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

public class CmImageBase implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @JSONField(name = "id")
    private String id;

    @JSONField(name = "type")
    private String type;

    @JSONField(name = "major")
    private Integer major;

    @JSONField(name = "minor")
    private Integer minor;

    @JSONField(name = "patch")
    private Integer patch;

    @JSONField(name = "build")
    private Integer build;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    @Override
    public String toString() {
        return "CmImageBase [id=" + id + ", type=" + type + ", major=" + major + ", minor=" + minor + ", patch=" + patch
                + ", build=" + build + "]";
    }

}
