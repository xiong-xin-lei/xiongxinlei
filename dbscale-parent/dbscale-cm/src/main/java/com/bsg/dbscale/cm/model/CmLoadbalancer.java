package com.bsg.dbscale.cm.model;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

public class CmLoadbalancer implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @JSONField(name = "service_id")
    private String serviceId;

    @JSONField(name = "ips")
    private List<String> ips;

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public List<String> getIps() {
        return ips;
    }

    public void setIps(List<String> ips) {
        this.ips = ips;
    }

    @Override
    public String toString() {
        return "CmLoadbalancer [serviceId=" + serviceId + ", ips=" + ips + "]";
    }

}
