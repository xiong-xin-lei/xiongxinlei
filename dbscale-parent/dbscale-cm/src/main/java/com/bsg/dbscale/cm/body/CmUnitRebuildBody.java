package com.bsg.dbscale.cm.body;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

public class CmUnitRebuildBody implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @JSONField(name = "node")
    private String node;

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    @Override
    public String toString() {
        return "CmUnitRebuildBody [node=" + node + "]";
    }

}
