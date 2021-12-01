package com.bsg.dbscale.cm.body;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

public class CmServicesLinkBody implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @JSONField(name = "service_group_type")
    private String serviceGroupType;

    @JSONField(name = "services")
    private List<String> serevies;

    public String getServiceGroupType() {
        return serviceGroupType;
    }

    public void setServiceGroupType(String serviceGroupType) {
        this.serviceGroupType = serviceGroupType;
    }

    public List<String> getSerevies() {
        return serevies;
    }

    public void setSerevies(List<String> serevies) {
        this.serevies = serevies;
    }

    @Override
    public String toString() {
        return "CmServicesLinkBody [serviceGroupType=" + serviceGroupType + ", serevies=" + serevies + "]";
    }

}
