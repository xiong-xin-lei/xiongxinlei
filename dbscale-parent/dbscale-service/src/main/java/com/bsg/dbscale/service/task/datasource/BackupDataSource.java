package com.bsg.dbscale.service.task.datasource;

import java.util.ArrayList;
import java.util.List;

public class BackupDataSource {
    private String backupStorageType;
    private String type;
    private List<String> tables;
    private Long expired;

    public BackupDataSource() {
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
        return "BackupDataSource [backupStorageType=" + backupStorageType + ", type=" + type + ", tables=" + tables
                + ", expired=" + expired + "]";
    }

}
