package com.bsg.dbscale.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DBUserDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String username;
    private String whiteIp;
    private List<DBPrivilegeDTO> dbPrivileges;

    public DBUserDTO() {
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

    public List<DBPrivilegeDTO> getDbPrivileges() {
        return dbPrivileges;
    }

    public void setDbPrivileges(List<DBPrivilegeDTO> dbPrivileges) {
        this.dbPrivileges = dbPrivileges;
    }

    @Override
    public String toString() {
        return "DBUserDTO [username=" + username + ", whiteIp=" + whiteIp + ", dbPrivileges=" + dbPrivileges + "]";
    }

}
