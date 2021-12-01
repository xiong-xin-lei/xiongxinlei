package com.bsg.dbscale.dao.domain;

import java.io.Serializable;
import java.util.Date;

public class BackupStrategyDO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String id;
    private String servGroupId;
    private String backupStorageType;
    private String type;
    private String tables;
    private String cronExpression;
    private Integer fileRetentionNum;
    private Boolean enabled;
    private String description;
    private Date gmtCreate;
    private String creator;
    private ServGroupDO servGroup;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServGroupId() {
        return servGroupId;
    }

    public void setServGroupId(String servGroupId) {
        this.servGroupId = servGroupId;
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

    public String getTables() {
        return tables;
    }

    public void setTables(String tables) {
        this.tables = tables;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public Integer getFileRetentionNum() {
        return fileRetentionNum;
    }

    public void setFileRetentionNum(Integer fileRetentionNum) {
        this.fileRetentionNum = fileRetentionNum;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public ServGroupDO getServGroup() {
        return servGroup;
    }

    public void setServGroup(ServGroupDO servGroup) {
        this.servGroup = servGroup;
    }

    @Override
    public String toString() {
        return "BackupStrategyDO [id=" + id + ", servGroupId=" + servGroupId + ", backupStorageType="
                + backupStorageType + ", type=" + type + ", tables=" + tables + ", cronExpression=" + cronExpression
                + ", fileRetentionNum=" + fileRetentionNum + ", enabled=" + enabled + ", description=" + description
                + ", gmtCreate=" + gmtCreate + ", creator=" + creator + ", servGroup=" + servGroup + "]";
    }

}
