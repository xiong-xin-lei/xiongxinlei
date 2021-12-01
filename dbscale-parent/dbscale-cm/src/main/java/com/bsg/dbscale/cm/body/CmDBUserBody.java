package com.bsg.dbscale.cm.body;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

public class CmDBUserBody implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @JSONField(name = "auth_type")
    private String authType;

    @JSONField(name = "name")
    private String name;

    @JSONField(name = "pwd")
    private String pwd;

    @JSONField(name = "ip")
    private String ip;

    @JSONField(name = "db_privileges")
    private List<DBPrivilege> dbPrivileges;

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public List<DBPrivilege> getDbPrivileges() {
        return dbPrivileges;
    }

    public void setDbPrivileges(List<DBPrivilege> dbPrivileges) {
        this.dbPrivileges = dbPrivileges;
    }

    @Override
    public String toString() {
        return "CmDBUserBody [authType=" + authType + ", name=" + name + ", pwd=" + pwd + ", ip=" + ip
                + ", dbPrivileges=" + dbPrivileges + "]";
    }

    public class DBPrivilege {

        @JSONField(name = "db_name")
        private String dbName;

        @JSONField(name = "privileges")
        private List<String> privileges;

        public String getDbName() {
            return dbName;
        }

        public void setDbName(String dbName) {
            this.dbName = dbName;
        }

        public List<String> getPrivileges() {
            return privileges;
        }

        public void setPrivileges(List<String> privileges) {
            this.privileges = privileges;
        }

        @Override
        public String toString() {
            return "DBPrivilege [dbName=" + dbName + ", privileges=" + privileges + "]";
        }

    }
}
