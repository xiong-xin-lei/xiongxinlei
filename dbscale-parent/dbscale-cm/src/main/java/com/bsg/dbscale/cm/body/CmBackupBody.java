package com.bsg.dbscale.cm.body;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

public class CmBackupBody implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @JSONField(name = "unit_id")
    private String untiId;

    @JSONField(name = "backup_storage_type")
    private String backupStorageType;

    @JSONField(name = "backup_storage_id")
    private String backupStorageId;

    @JSONField(name = "type")
    private String type;

    @JSONField(name = "expired_at")
    private Long expiredAt;

    public String getUntiId() {
        return untiId;
    }

    public void setUntiId(String untiId) {
        this.untiId = untiId;
    }

    public String getBackupStorageType() {
        return backupStorageType;
    }

    public void setBackupStorageType(String backupStorageType) {
        this.backupStorageType = backupStorageType;
    }

    public String getBackupStorageId() {
        return backupStorageId;
    }

    public void setBackupStorageId(String backupStorageId) {
        this.backupStorageId = backupStorageId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(Long expiredAt) {
        this.expiredAt = expiredAt;
    }

    @Override
    public String toString() {
        return "CmBackupBody [untiId=" + untiId + ", backupStorageType=" + backupStorageType + ", backupStorageId="
                + backupStorageId + ", type=" + type + ", expiredAt=" + expiredAt + "]";
    }

}
