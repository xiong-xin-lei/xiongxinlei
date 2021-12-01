package com.bsg.dbscale.cm.body;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

public class CmUnitRestoreBody implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @JSONField(name = "backup_file_id")
    private String backupFileId;

    public String getBackupFileId() {
        return backupFileId;
    }

    public void setBackupFileId(String backupFileId) {
        this.backupFileId = backupFileId;
    }

    @Override
    public String toString() {
        return "CmUnitRestoreBody [backupFileId=" + backupFileId + "]";
    }

}
