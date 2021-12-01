package com.bsg.dbscale.cm.model;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

public class CmServiceArchBase implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @JSONField(name = "mode")
    private String mode;

    @JSONField(name = "replicas")
    private Integer replicas;

    @JSONField(name = "desc")
    private String desc;

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Integer getReplicas() {
        return replicas;
    }

    public void setReplicas(Integer replicas) {
        this.replicas = replicas;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "CmServiceArchBase [mode=" + mode + ", replicas=" + replicas + ", desc=" + desc + "]";
    }

}
