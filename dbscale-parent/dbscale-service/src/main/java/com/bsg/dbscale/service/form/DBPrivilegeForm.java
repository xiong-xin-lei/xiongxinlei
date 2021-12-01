package com.bsg.dbscale.service.form;

import java.io.Serializable;
import java.util.List;

public class DBPrivilegeForm implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private String dbName;
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
        return "DBPrivilegeForm [dbName=" + dbName + ", privileges=" + privileges + "]";
    }

}
