package com.bsg.dbscale.cm.body;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

public class CmBackupExternalBody implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @JSONField(name = "service_id")
    private String serviceId;

    @JSONField(name = "storage_type")
    private String backupStorageType;

    @JSONField(name = "storage_provider_id")
    private String backupStorageId;

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
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

    @Override
    public String toString() {
        return "CmBackupExternalBody [serviceId=" + serviceId + ", backupStorageType=" + backupStorageType
                + ", backupStorageId=" + backupStorageId + "]";
    }

}
