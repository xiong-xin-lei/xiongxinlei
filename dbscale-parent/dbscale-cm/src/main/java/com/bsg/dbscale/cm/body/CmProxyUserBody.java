package com.bsg.dbscale.cm.body;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

public class CmProxyUserBody implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @JSONField(name = "username")
    private String username;

    @JSONField(name = "pwd")
    private String pwd;

    @JSONField(name = "max_connections")
    private Integer maxConnection;

    @JSONField(name = "default_hostgroup")
    private Integer hostGroup;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public Integer getMaxConnection() {
        return maxConnection;
    }

    public void setMaxConnection(Integer maxConnection) {
        this.maxConnection = maxConnection;
    }

    public Integer getHostGroup() {
        return hostGroup;
    }

    public void setHostGroup(Integer hostGroup) {
        this.hostGroup = hostGroup;
    }

    @Override
    public String toString() {
        return "CmProxyUserBody [username=" + username + ", pwd=" + pwd + ", maxConnection=" + maxConnection
                + ", hostGroup=" + hostGroup + "]";
    }

}
