package com.bsg.dbscale.cm.model;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

public class CmDBUser implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @JSONField(name = "name")
    private String name;

    @JSONField(name = "auth_type")
    private String authType;

    @JSONField(name = "ip")
    private String ip;

    @JSONField(name = "db_privileges")
    private List<DbPrivilege> dbPrivileges;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public List<DbPrivilege> getDbPrivileges() {
        return dbPrivileges;
    }

    public void setDbPrivileges(List<DbPrivilege> dbPrivileges) {
        this.dbPrivileges = dbPrivileges;
    }

    @Override
    public String toString() {
        return "CmDBUser [name=" + name + ", authType=" + authType + ", ip=" + ip + ", dbPrivileges=" + dbPrivileges
                + "]";
    }

    public class DbPrivilege {
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
            return "DbPrivilege [dbName=" + dbName + ", privileges=" + privileges + "]";
        }

    }

}
