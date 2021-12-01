package com.bsg.dbscale.dao.query;

import java.io.Serializable;

public class HostQuery implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String clusterId;

    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }

    @Override
    public String toString() {
        return "HostQuery [clusterId=" + clusterId + "]";
    }

}
