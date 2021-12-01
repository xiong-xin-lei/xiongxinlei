package com.bsg.dbscale.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MysqlServGroupUserDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String username;
    private String whiteIp;
    private Integer maxConnection;
    private DisplayDTO properties;
    private List<DBPrivilegeDTO> dbPrivileges;

    public MysqlServGroupUserDTO() {
        this.dbPrivileges = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getWhiteIp() {
        return whiteIp;
    }

    public void setWhiteIp(String whiteIp) {
        this.whiteIp = whiteIp;
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

    public List<DBPrivilegeDTO> getDbPrivileges() {
        return dbPrivileges;
    }

    public void setDbPrivileges(List<DBPrivilegeDTO> dbPrivileges) {
        this.dbPrivileges = dbPrivileges;
    }

    @Override
    public String toString() {
        return "MysqlServGroupUserDTO [username=" + username + ", whiteIp=" + whiteIp + ", maxConnection="
                + maxConnection + ", properties=" + properties + ", dbPrivileges=" + dbPrivileges + "]";
    }

}
