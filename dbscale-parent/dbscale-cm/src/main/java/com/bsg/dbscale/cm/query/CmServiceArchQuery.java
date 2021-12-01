package com.bsg.dbscale.cm.query;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

public class CmServiceArchQuery implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @JSONField(name = "type")
    private String type;

    @JSONField(name = "version")
    private String version;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "CmServiceArchQuery [type=" + type + ", version=" + version + "]";
    }

}
