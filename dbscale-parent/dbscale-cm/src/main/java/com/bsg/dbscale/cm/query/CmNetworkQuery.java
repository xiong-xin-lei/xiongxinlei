package com.bsg.dbscale.cm.query;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

public class CmNetworkQuery implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @JSONField(name = "id")
    private String id;

    @JSONField(name = "zone")
    private String zone;

    @JSONField(name = "name")
    private String name;

    @JSONField(name = "site_id")
    private String siteId;

    @JSONField(name = "topology")
    private String topology;

    @JSONField(name = "unschedulable")
    private Boolean unschedulable;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getTopology() {
        return topology;
    }

    public void setTopology(String topology) {
        this.topology = topology;
    }

    public Boolean getUnschedulable() {
        return unschedulable;
    }

    public void setUnschedulable(Boolean unschedulable) {
        this.unschedulable = unschedulable;
    }

    @Override
    public String toString() {
        return "CmNetworkQuery [id=" + id + ", zone=" + zone + ", name=" + name + ", siteId=" + siteId + ", topology="
                + topology + ", unschedulable=" + unschedulable + "]";
    }

}
