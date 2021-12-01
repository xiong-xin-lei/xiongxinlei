package com.bsg.dbscale.cm.model;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

public class CmServiceArch extends CmServiceArchBase implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @JSONField(name = "service_type")
    private String serviceType;

    @JSONField(name = "service_version")
    private String serviceVersion;

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getServiceVersion() {
        return serviceVersion;
    }

    public void setServiceVersion(String serviceVersion) {
        this.serviceVersion = serviceVersion;
    }

    @Override
    public String toString() {
        return super.toString() + "CmServiceArch [serviceType=" + serviceType + ", serviceVersion=" + serviceVersion
                + "]";
    }

}
