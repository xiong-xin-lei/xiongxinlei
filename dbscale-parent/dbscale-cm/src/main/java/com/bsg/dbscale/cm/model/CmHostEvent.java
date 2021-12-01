package com.bsg.dbscale.cm.model;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

public class CmHostEvent implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @JSONField(name = "host")
    private List<CmEvent> hosts;

    @JSONField(name = "node")
    private List<CmEvent> nodes;

    public List<CmEvent> getHosts() {
        return hosts;
    }

    public void setHosts(List<CmEvent> hosts) {
        this.hosts = hosts;
    }

    public List<CmEvent> getNodes() {
        return nodes;
    }

    public void setNodes(List<CmEvent> nodes) {
        this.nodes = nodes;
    }

    @Override
    public String toString() {
        return "CmHostEvent [hosts=" + hosts + ", nodes=" + nodes + "]";
    }

}
