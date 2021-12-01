package com.bsg.dbscale.cm.model;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

public class CmProxyUser implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @JSONField(name = "username")
    private String username;

    @JSONField(name = "max_connections")
    private Integer maxConnection;

    @JSONField(name = "default_hostgroup")
    private Integer hostgroup;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getMaxConnection() {
        return maxConnection;
    }

    public void setMaxConnection(Integer maxConnection) {
        this.maxConnection = maxConnection;
    }

    public Integer getHostgroup() {
        return hostgroup;
    }

    public void setHostgroup(Integer hostgroup) {
        this.hostgroup = hostgroup;
    }

    @Override
    public String toString() {
        return "CmProxyUser [username=" + username + ", maxConnection=" + maxConnection + ", hostgroup=" + hostgroup
                + "]";
    }

}
