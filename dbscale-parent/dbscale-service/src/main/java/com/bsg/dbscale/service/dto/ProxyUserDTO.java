package com.bsg.dbscale.service.dto;

import java.io.Serializable;

public class ProxyUserDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String username;
    private Integer maxConnection;
    private DisplayDTO properties;

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

    public DisplayDTO getProperties() {
        return properties;
    }

    public void setProperties(DisplayDTO properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        return "ProxyUserDTO [username=" + username + ", maxConnection=" + maxConnection + ", properties=" + properties
                + "]";
    }

}
