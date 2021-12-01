package com.bsg.dbscale.service.dto;

import java.io.Serializable;

public class BackupFileDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private Long size;
    private DisplayDTO type;
    private DisplayDTO backupStorageType;
    private DisplayDTO status;
    private String msg;
    private String directory;
    private Boolean external;
    private String createdAt;
    private String finishAt;
    private String expiredAt;
    private String unitName;
    private String serviceName;
    private IdentificationDTO site;

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

    public DisplayDTO getType() {
        return type;
    }

    public void setType(DisplayDTO type) {
        this.type = type;
    }

    public DisplayDTO getBackupStorageType() {
        return backupStorageType;
    }

    public void setBackupStorageType(DisplayDTO backupStorageType) {
        this.backupStorageType = backupStorageType;
    }

    public DisplayDTO getStatus() {
        return status;
    }

    public void setStatus(DisplayDTO status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
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

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public IdentificationDTO getSite() {
        return site;
    }

    public void setSite(IdentificationDTO site) {
        this.site = site;
    }

    @Override
    public String toString() {
        return "BackupFileDTO [id=" + id + ", name=" + name + ", size=" + size + ", type=" + type
                + ", backupStorageType=" + backupStorageType + ", status=" + status + ", msg=" + msg + ", directory="
                + directory + ", external=" + external + ", createdAt=" + createdAt + ", finishAt=" + finishAt
                + ", expiredAt=" + expiredAt + ", unitName=" + unitName + ", serviceName=" + serviceName + ", site="
                + site + "]";
    }

}
