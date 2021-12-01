package com.bsg.dbscale.cm.body;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

public class CmRemoteStoragePoolBody implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @JSONField(name = "name")
    private String name;

    @JSONField(name = "performance")
    private String performance;

    @JSONField(name = "unschedulable")
    private Boolean unschedulable;

    @JSONField(name = "desc")
    private String desc;

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

    public Boolean getUnschedulable() {
        return unschedulable;
    }

    public void setUnschedulable(Boolean unschedulable) {
        this.unschedulable = unschedulable;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "CmRemoteStoragePoolBody [name=" + name + ", performance=" + performance + ", unschedulable="
                + unschedulable + ", desc=" + desc + "]";
    }

}
