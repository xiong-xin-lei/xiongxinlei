package com.bsg.dbscale.service.form;

import java.io.Serializable;

public class ServImageForm implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String type;
    private VersionForm version;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public VersionForm getVersion() {
        return version;
    }

    public void setVersion(VersionForm version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "ServImageForm [type=" + type + ", version=" + version + "]";
    }

}
