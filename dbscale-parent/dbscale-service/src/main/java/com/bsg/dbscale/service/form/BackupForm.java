package com.bsg.dbscale.service.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BackupForm implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String backupStorageType;
    private String type;
    private List<String> tables;
    private Long expired;

    public BackupForm() {
        tables = new ArrayList<>();
    }

    public String getBackupStorageType() {
        return backupStorageType;
    }

    public void setBackupStorageType(String backupStorageType) {
        this.backupStorageType = backupStorageType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getTables() {
        return tables;
    }

    public void setTables(List<String> tables) {
        this.tables = tables;
    }

    public Long getExpired() {
        return expired;
    }

    public void setExpired(Long expired) {
        this.expired = expired;
    }

    @Override
    public String toString() {
        return "BackupForm [backupStorageType=" + backupStorageType + ", type=" + type + ", tables=" + tables
                + ", expired=" + expired + "]";
    }

}
