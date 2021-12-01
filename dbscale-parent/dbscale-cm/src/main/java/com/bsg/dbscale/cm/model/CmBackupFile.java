package com.bsg.dbscale.cm.model;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

public class CmBackupFile implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @JSONField(name = "id")
    private String id;

    @JSONField(name = "file_name")
    private String name;

    @JSONField(name = "size")
    private Long size;

    @JSONField(name = "unit")
    private String unit;

    @JSONField(name = "service")
    private String service;

    @JSONField(name = "site")
    private CmSiteBase site;

    @JSONField(name = "storage_type")
    private String backupStorageType;

    @JSONField(name = "type")
    private String type;

    @JSONField(name = "directory")
    private String directory;

    @JSONField(name = "status")
    private String status;

    @JSONField(name = "msg")
    private String msg;

    @JSONField(name = "external")
    private Boolean external;

    @JSONField(name = "created_at")
    private String createdAt;

    @JSONField(name = "finish_at")
    private String finishAt;

    @JSONField(name = "expired_at")
    private String expiredAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public CmSiteBase getSite() {
        return site;
    }

    public void setSite(CmSiteBase site) {
        this.site = site;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Boolean getExternal() {
        return external;
    }

    public void setExternal(Boolean external) {
        this.external = external;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getFinishAt() {
        return finishAt;
    }

    public void setFinishAt(String finishAt) {
        this.finishAt = finishAt;
    }

    public String getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(String expiredAt) {
        this.expiredAt = expiredAt;
    }

    @Override
    public String toString() {
        return "CmBackupFile [id=" + id + ", name=" + name + ", size=" + size + ", unit=" + unit + ", service="
                + service + ", site=" + site + ", backupStorageType=" + backupStorageType + ", type=" + type
                + ", directory=" + directory + ", status=" + status + ", msg=" + msg + ", external=" + external
                + ", createdAt=" + createdAt + ", finishAt=" + finishAt + ", expiredAt=" + expiredAt + "]";
    }

}
