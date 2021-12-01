package com.bsg.dbscale.service.form;

import java.io.Serializable;

public class UnitRebuildInitForm implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String backupStorageType;
    private String backupFileId;

    public String getBackupStorageType() {
        return backupStorageType;
    }

    public void setBackupStorageType(String backupStorageType) {
        this.backupStorageType = backupStorageType;
    }

    public String getBackupFileId() {
        return backupFileId;
    }

    public void setBackupFileId(String backupFileId) {
        this.backupFileId = backupFileId;
    }

    @Override
    public String toString() {
        return "UnitRebuildInitForm [backupStorageType=" + backupStorageType + ", backupFileId=" + backupFileId + "]";
    }

}
