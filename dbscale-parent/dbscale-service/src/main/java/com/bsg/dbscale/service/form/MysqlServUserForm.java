package com.bsg.dbscale.service.form;

import java.io.Serializable;
import java.util.List;

public class MysqlServUserForm implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String authType;
    private String username;
    private String password;
    private List<String> whiteIps;
    private List<DBPrivilegeForm> dbPrivileges;

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getWhiteIps() {
        return whiteIps;
    }

    public void setWhiteIps(List<String> whiteIps) {
        this.whiteIps = whiteIps;
    }

    public List<DBPrivilegeForm> getDbPrivileges() {
        return dbPrivileges;
    }

    public void setDbPrivileges(List<DBPrivilegeForm> dbPrivileges) {
        this.dbPrivileges = dbPrivileges;
    }

    @Override
    public String toString() {
        return "MysqlServUserForm [authType=" + authType + ", username=" + username + ", password=" + password
                + ", whiteIps=" + whiteIps + ", dbPrivileges=" + dbPrivileges + "]";
    }

}
