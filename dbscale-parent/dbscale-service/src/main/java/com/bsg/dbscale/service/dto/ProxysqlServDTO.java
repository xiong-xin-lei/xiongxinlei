package com.bsg.dbscale.service.dto;

import java.io.Serializable;
import java.util.List;

public class ProxysqlServDTO extends ServDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private List<String> loadbalanceIps;

    public List<String> getLoadbalanceIps() {
        return loadbalanceIps;
    }

    public void setLoadbalanceIps(List<String> loadbalanceIps) {
        this.loadbalanceIps = loadbalanceIps;
    }

    @Override
    public String toString() {
        return "ProxysqlServDTO [loadbalanceIps=" + loadbalanceIps + "]";
    }

}
